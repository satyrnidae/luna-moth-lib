package dev.satyrn.lunamoth.util.v1;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Represents a {@link ResourceBundle} that uses a Java properties formatted *.lang file as underlying data source.
 *
 * @author Isabel Maskrey
 * @since 1.0-SNAPSHOT
 */
// TODO: Could be useful outside of Paper specifically. Extract to Moon Moth common library
public class LanguageResourceBundle extends PropertyResourceBundle {
    /**
     * Creates a new language file resource bundle with the UTF-8 standard charset.
     *
     * @param stream The stream to read.
     * @throws IOException if an error occurred when reading resources using any I/O operation.
     * @since 1.0-SNAPSHOT
     */
    public LanguageResourceBundle(final @NotNull InputStream stream) throws IOException {
        super(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }

    /**
     * Creates a new language file resource bundle from a stream reader. Unlike the constructor
     * {@link #LanguageResourceBundle(InputStream)}, there is no limitation as to the encoding of the input language
     * file.
     *
     * @param reader a reader that represents a language file to read from.
     * @throws IOException if an error occurs when reading resources using any I/O operation.
     * @since 1.0-SNAPSHOT
     */
    @SuppressWarnings({"unused"})
    public LanguageResourceBundle(InputStreamReader reader) throws IOException {
        super(reader);
    }

    /**
     * Used in the factory method {@link ResourceBundle#getBundle(String, Locale, ClassLoader, ResourceBundle.Control)}
     * to get a new instance of {@link LanguageResourceBundle}.
     * <p>
     * See {@link ResourceBundle.Control} for more details.
     *
     * @author Isabel Maskrey
     * @since 1.0-SNAPSHOT
     */
    public final static class Control extends AbstractResourceFileControl<LanguageResourceBundle> {
        /**
         * Represents a resource bundle loader that handles Java Properties formatted *.lang files
         * <p>
         * In particular, {@link #toBundleName(String, Locale)} is overridden to return a bundle name in the format
         * &lt;language&gt;_&lt;country code&gt; such that the locale {@link Locale#US} would be formatted as
         * {@code en_us}.
         * <p>
         * Additionally, resource files are loaded by a custom resource name with a specific file extension, in this case,
         * {@code "lang"}.
         *
         * @since 1.0-SNAPSHOT
         */
        public Control() {
            this("lang");
        }

        /**
         * Represents a resource bundle loader that handles Java Properties formatted files with a custom file extension.
         * <p>
         * In particular, {@link #toBundleName(String, Locale)} is overridden to return a bundle name in the format
         * &lt;language&gt;_&lt;country code&gt; such that the locale {@link Locale#US} would be formatted as
         * {@code en_us}.
         * <p>
         * Additionally, resource files are loaded by a custom resource name with a specific file extension, e.g.
         * {@code "lang"}.
         *
         * @param fileExtension The file extension that this instance should handle. As this is a properties-style
         *                      resource handler, this file type should generally be either "lang" or "properties".
         * @since 1.0-SNAPSHOT
         */
        public Control(String fileExtension) {
            super(fileExtension);
        }

        /**
         * Gets a new resource bundle of the type handled by this control file.
         *
         * @param stream The resource stream for the resource.
         * @return       The new resource bundle from the stream.
         * @throws IOException if an error occurred when reading resources using any I/O operation.
         * @since 1.0-SNAPSHOT
         */
        @Override
        protected @NotNull LanguageResourceBundle newBundle(final @NotNull InputStream stream) throws IOException {
            return new LanguageResourceBundle(stream);
        }
    }
}
