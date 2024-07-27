package dev.satyrn.lunamoth.util.v1;

import dev.satyrn.lunamoth.i18n.v1.I18n;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Abstract base class for loading resource files with a specific file extension into a resource bundle.
 *
 * @param <T> The resource bundle type.
 * @implSpec  Implementations should provide the supported file extension (e.g., "lang" or "json") and handle specific
 *            file formats for those extensions.
 * @author    Isabel Maskrey
 * @since     1.0-SNAPSHOT
 */
public abstract class AbstractResourceFileControl<T extends ResourceBundle> extends ResourceBundle.Control {
    /**
     * The file extension that will be used to load resource files in a format supported by this loader.
     *
     * @since 1.0-SNAPSHOT
     */
    private final @NotNull String fileExtension;

    /**
     * Represents a resource bundle loader that handles different file types.
     * <p>
     * In particular, {@link #toBundleName(String, Locale)} is overriden to return a bundle name in the format
     * &lt;language&gt;_&lt;country code&gt; such that the locale {@link Locale#US} would be formatted as {@code en_us}.
     * <p>
     * Additionally, resource files are loaded by a custom resource name with a specific file extension, specified by
     * {@code fileExtension}.
     *
     * @param fileExtension the file extension for the resource files handled by this file control.
     * @since               1.0-SNAPSHOT
     */
    protected AbstractResourceFileControl(final @NotNull String fileExtension) {
        Objects.requireNonNull(fileExtension);
        this.fileExtension = fileExtension;
    }

    /**
     * Gets and converts the resource file loader's bundle name to the form required by the
     * {@link ClassLoader#getResource(String)} method by replacing all occurrences of {@code '.'} in the bundle name
     * with {@code '/'} and appending a {@code '.'} and the file suffix specified by {@code fileExtension}.
     * <p>
     * For example, if the {@code baseName} is {@code "plugin_name.lang"}, the locale is {@link Locale#US}, then the
     * bundle name will resolve to {@code "plugin_name.lang.en_us} (see {@link #toBundleName(String, Locale)} for more
     * details on this). If {@code fileExtension} is set to {@code "json"}, then the final value of the resource name
     * {@code "plugin_name/lang/en_us.json"} is returned.
     *
     * @param    baseName the base name of the resource bundle, a fully qualified class name
     * @param    locale   the locale for which a resource bundle should be loaded
     * @return   the {@code bundleName} of this resource bungle, converted to a form required by the
     *           {@link ClassLoader#getResource(String)} method.
     * @throws   NullPointerException if {@code baseName} is {@code null} or {@code locale} is {@code null}
     * @implNote This is the preferred method of obtaining the {@code resourceName} of the bundle. Implementing
     *           classes may still call the base {@link #toResourceName(String, String)} method with a custom file
     *           suffix, but this is discouraged.
     * @since    1.0-SNAPSHOT
     * @see      #toBundleName(String, Locale)
     */
    protected final @NotNull String toResourceName(String baseName, Locale locale) {
        return this.toResourceName(this.toBundleName(baseName, locale), this.fileExtension);
    }

    /**
     * Gets the bundle name of this resource.
     * <p>
     * Lang file bundle names are always the language code, an underscore, and the lowercase country code. For example,
     * for the US English locale, the bundle name will be {@code en_us}.
     * <p>
     * If the {@code locale} does not specify a country code, the underscore and country code are omitted.
     * <p>
     * The only time when this is not the case is for the root locale, or if the {@code locale} does not specify a
     * language. In these instances, the {@code baseName} is returned instead.
     *
     * @param  baseName the base name of the resource bundle, a fully qualified class name
     * @param  locale   the locale for which a resource bundle should be loaded
     * @return The locale's language and country code separated by an underscore or, if the locale lacks a code, returns
     *         the base name.
     * @throws NullPointerException if {@code baseName} is {@code null} or {@code locale} is {@code null}.
     * @since          1.0-SNAPSHOT
     */
    @Contract("_, _ -> !null")
    @Override
    public final @NotNull String toBundleName(final @NotNull String baseName,
                                              final @NotNull Locale locale) {
        Objects.requireNonNull(baseName);
        Objects.requireNonNull(locale);
        if (locale == Locale.ROOT || locale.getLanguage() == null || locale.getLanguage().isBlank()) {
            return baseName;
        }

        return baseName + "." + I18n.getLocaleString(locale);
    }

    /**
     * Creates a new resource bundle from a file stream
     *
     * @param baseName the base bundle name of the resource bundle, a fully qualified class name
     * @param locale   the locale for which the resource bundle should be instantiated
     * @param format   the resource bundle format to be loaded
     * @param loader   the {@code ClassLoader} to use to load the bundle
     * @param reload   the flag to indicate bundle reloading; {@code true} if reloading an expired resource bundle,
     *                 {@code false} otherwise
     * @return a new {@link PropertyResourceBundle} encapsulating the contents of the *.lang file.
     * @throws NullPointerException     if {@code baseName}, {@code locale}, or {@code loader} is null.
     * @throws IOException              if an error occurred when reading resources using any I/O operation.
     * @throws MissingResourceException if the resource bundle could not be found.
     * @since  1.0-SNAPSHOT
     */
    @Contract("_, _, _, _, _ -> !null")
    @Override
    public final @NotNull ResourceBundle newBundle(final @NotNull String baseName,
                                                   final @NotNull Locale locale,
                                                   final @Nullable String format,
                                                   final @NotNull ClassLoader loader,
                                                   final boolean reload) throws IOException, MissingResourceException {
        final @NotNull String resourceName = this.toResourceName(baseName, locale);

        try (final @Nullable InputStream stream = reload ? getReloadedResourceAsStream(loader, resourceName) : loader.getResourceAsStream(resourceName)) {
            if (stream != null) {
                return this.newBundle(stream);
            }
        }

        throw new MissingResourceException("Could not find resource bundle for baseName " + baseName + " and locale " + locale, baseName, "");
    }

    /**
     * Retrieves a reloaded input stream for a resource bundle.
     *
     * @param  loader       the {@link ClassLoader} to use to load the bundle.
     * @param  resourceName the name of the resource to load.
     * @return an {@code InputStream} for the resource, or {@code null} if the resource could not be found.
     * @throws NullPointerException if {@code loader} is {@code null} or {@code resoruceName} is {@code null}.
     * @throws IOException          if an error occurred when reading resources using any I/O operation.
     * @since  1.0-SNAPSHOT
     */
    @Contract("_, _ -> _")
    protected final @Nullable InputStream getReloadedResourceAsStream(final @NotNull ClassLoader loader,
                                                                      final @NotNull String resourceName) throws IOException {
        Objects.requireNonNull(loader);
        final @Nullable URL url = loader.getResource(resourceName);
        if (url != null) {
            final @Nullable URLConnection connection = url.openConnection();
            if (connection != null) {
                connection.setUseCaches(false);
                return connection.getInputStream();
            }
        }
        return null;
    }

    /**
     * Gets a new resource bundle of the type handled by this control file.
     *
     * @param stream The resource stream for the resource.
     * @return       The new resource bundle from the stream.
     * @throws IOException if an error occurred when reading resources using any I/O operation.
     * @since 1.0-SNAPSHOT
     */
    @Contract("_ -> !null")
    protected abstract @NotNull T newBundle(final @NotNull InputStream stream) throws IOException;
}
