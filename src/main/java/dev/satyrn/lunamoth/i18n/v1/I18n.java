package dev.satyrn.lunamoth.i18n.v1;

import dev.satyrn.lunamoth.lang.v1.FileResourceLoader;
import dev.satyrn.lunamoth.util.v1.JsonResourceBundle;
import dev.satyrn.lunamoth.util.v1.LanguageResourceBundle;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static java.util.ResourceBundle.Control;

/**
 * Provides internationalization support through a {@link ResourceBundle}.
 *
 * @author Isabel Maskrey
 * @since  1.0.0
 */
public class I18n {
    private final static @NotNull ClassLoader DEFAULT_CLASS_LOADER = I18n.class.getClassLoader();
    private final static @NotNull Control LANG_FILE_CONTROL = new LanguageResourceBundle.Control();
    private final static @NotNull Control JSON_FILE_CONTROL = new JsonResourceBundle.Control();
    private final static @NotNull Pattern DOUBLE_APOS = Pattern.compile("''");
    private final static @NotNull Logger I18N_LOGGER = Logger.getLogger(I18n.class.getName());
    private final static @NotNull Locale DEFAULT_LOCALE = Locale.US;
    private final @NotNull String baseName;
    private final @NotNull HashMap<String, HashMap<String, MessageFormat>> formatters = HashMap.newHashMap(2);
    private @NotNull Locale currentLocale = DEFAULT_LOCALE;
    private @NotNull Control resourceControl = LANG_FILE_CONTROL;
    private @Nullable ResourceBundle internalTranslations;
    private @Nullable ResourceBundle externalTranslations;
    private @Nullable ResourceBundle defaultTranslations;
    private @Nullable FileResourceLoader fileResourceLoader;
    private @Nullable String baseDirectory;

    /**
     * Creates a new {@link I18n} instance which only relies on internal resources.
     *
     * @param  baseName The base package name for I18n resource key/value files.
     * @throws NullPointerException if {@code baseName} is {@code null}
     * @since  1.0-SNAPSHOT
     * @see    #I18n(String, String)
     */
    public I18n(final @NotNull String baseName) {
        this(baseName, null);
    }

    /**
     * Creates a new {@link I18n} instance with optional external directory translation definition.
     *
     * @param  baseName      the base package name for I18n resource key/value files
     * @param  baseDirectory the base directory where fallback or user-configured custom translations are stored. Can be
     *                       {@code null} if the i18n utility should not support this feature.
     * @throws NullPointerException if {@code baseName} is {@code null}
     * @since  1.0-SNAPSHOT
     * @see    #I18n(String)
     */
    public I18n(final @NotNull  String baseName,
                final @Nullable String baseDirectory) {
        Objects.requireNonNull(baseName);
        this.baseName = baseName;
        this.baseDirectory = baseDirectory;
        this.reloadResources();
    }

    /**
     * Gets the base resource name for this {@code I18n}.
     *
     * @return The base package name for internationalization resources.
     * @since  1.0-SNAPSHOT
     */
    public @NotNull String getBaseName() {
        return this.baseName;
    }

    /**
     * Gets the base directory for the file handler
     *
     * @return The base directory
     */
    public @Nullable String getBaseDirectory() {
        return this.baseDirectory;
    }

    /**
     * Gets the current locale for this {@code I18n}.
     *
     * @return The current locale used for translation.
     * @since 1.0-SNAPSHOT
     */
    public @NotNull Locale getCurrentLocale() {
        return this.currentLocale;
    }

    /**
     * Gets the locale's name in the format &lt;language&gt;_&lt;countryCode&gt;. For example, for US English, this
     * function returns the string {@code "en_us"}.
     *
     * @param  locale The locale to represent as a locale string
     * @return        The locale name in the format &lt;language&gt;_&lt;countryCode&gt;
     * @throws NullPointerException if {@code locale} is {@code null}
     * @since 1.0-SNAPSHOT
     */
    public static @NotNull String getLocaleString(final @NotNull Locale locale) {
        Objects.requireNonNull(locale);
        final @NotNull StringBuilder localeString = new StringBuilder(locale.getLanguage());
        if (locale.getCountry() != null && !locale.getCountry().isBlank()) {
            localeString.append("_").append(locale.getCountry().toLowerCase());
        }

        return localeString.toString();
    }

    /**
     * Sets the current {@code Locale} to use while loading translations.
     *
     * @param  locale the {@code Locale} to use while loading translations.
     * @return The modified {@code I18n} instance.
     * @throws NullPointerException if {@code locale} is {@code null}
     * @since 1.0-SNAPSHOT
     */
    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull I18n setCurrentLocale(final @NotNull Locale locale) {
        Objects.requireNonNull(locale);
        this.currentLocale = locale;
        this.reloadResources();
        return this;
    }

    /**
     * Sets the current {@code baseDirectory}.
     * <p>
     * If set to {@code null}, external resources will not be loaded.
     *
     * @param baseDirectory The base directory for the external resources.
     * @return The modified {@code I18n} instance.
     * @since  1.0-SNAPSHOT
     */
    @SuppressWarnings({"UnusedReturnValue"})
    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull I18n setBaseDirectory(final @Nullable String baseDirectory) {
        this.baseDirectory = baseDirectory;
        this.reloadResources();
        return this;
    }

    /**
     * Sets the current {@code Locale} to use while loading translations.
     * <p>
     * This function accepts a {@code localeString} in the format &lt;language&gt;_&lt;countryCode&gt;. For example, for
     * US English, you should pass the value {@code "en_us"}.
     * <p>
     * If {@code localeString} is {@code null} or an empty string, the default locale will be used (in this case, US
     * English).
     *
     * @param  localeString The {@code Locale} to use while loading translations.
     * @return The modified {@code I18n} instance.
     * @since  1.0-SNAPSHOT
     */
    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull I18n setCurrentLocale(final @Nullable String localeString) {
        if (Objects.isNull(localeString) || localeString.isBlank()) {
            this.currentLocale = DEFAULT_LOCALE;
        } else {
            final @NotNull String[] parts = Arrays.stream(localeString.split("_")).filter(part -> !part.isBlank()).toArray(String[]::new);
            if (parts.length == 0) {
                this.currentLocale = DEFAULT_LOCALE;
            } else if (parts.length == 1) {
                this.currentLocale = Locale.of(parts[0]);
            } else {
                this.currentLocale = Locale.of(parts[0], parts[1]);
            }
        }
        this.reloadResources();
        return this;
    }

    /**
     * Sets the {@code I18n} resource handlers to use a specific format.
     * <p>
     * Passing {@link ResourceType#CUSTOM} to this method will result in an {@code IllegalArgumentException}, as you
     * cannot specify {@code CUSTOM} as a {@code resourceType} without also specifying a {@code customResourceControl}.
     * See {@link #setResourceType(ResourceType, Control)} for more information on using custom resource types.
     *
     * @param resourceType The resource type. May be {@code LANG} or {@code JSON}, but not {@code CUSTOM}.
     * @return The modified {@code I18n} instance.
     * @throws NullPointerException if {@code resourceType} is {@code null}.
     * @throws IllegalArgumentException if {@code resourceType} is {@code custom}.
     * @since 1.0-SNAPSHOT
     */
    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull I18n setResourceType(final @NotNull ResourceType resourceType) {
        return this.setResourceType(resourceType, null);
    }

    /**
     * Sets the {@code I18n} resource handlers to use a specific format.
     * <p>
     * Passing {@code resourceType} as {@link ResourceType#CUSTOM} to this method without also supplying a
     * {@code customResourceControl} will result in an {@code IllegalArgumentException}. However, when setting
     * {@code resourceType} to {@link ResourceType#LANG} or {@link ResourceType#JSON}, {@code customResourceControl} is
     * ignored.
     *
     * @param resourceType The resource type.
     * @param customResourceControl The custom resource control for the {@code CUSTOM} {@code resourceType}. Only used
     *                              if {@code resourceType} is set to {@code CUSTOM}, in which case it is required.
     * @return The modified {@code I18n} instance.
     * @throws NullPointerException if {@code resourceType} is {@code null}
     * @throws IllegalArgumentException if {@code resourceType} is {@code CUSTOM} but {@code customResourceControl} is
     *                                  {@code null}
     * @since 1.0-SNAPSHOT
     */
    @Contract(value = "_, _ -> this", mutates = "this")
    public @NotNull I18n setResourceType(final @NotNull  ResourceType resourceType,
                                         final @Nullable ResourceBundle.Control customResourceControl) {
        Objects.requireNonNull(resourceType);
        switch (resourceType) {
            case JSON:
                this.resourceControl = JSON_FILE_CONTROL;
                break;
            case LANG:
                this.resourceControl = LANG_FILE_CONTROL;
                break;
            case CUSTOM:
                if (customResourceControl == null) {
                    throw new IllegalArgumentException("resourceType cannot be CUSTOM without a specified customResourceControl");
                }
                this.resourceControl = customResourceControl;
        }
        this.reloadResources();
        return this;
    }

    /**
     * Reloads the translations based on the current state of the {@code I18n}
     *
     * @apiNote This also includes an explicit call to {@link ResourceBundle#clearCache(ClassLoader)}. Use sparingly, if
     *          at all...
     * @since   1.0-SNAPSHOT
     */
    @Contract(mutates = "this")
    private void reloadResources() {

        ResourceBundle.clearCache(DEFAULT_CLASS_LOADER);
        if (this.fileResourceLoader != null) {
            ResourceBundle.clearCache(this.fileResourceLoader);
        }

        try {
            this.defaultTranslations = ResourceBundle.getBundle(this.baseName, DEFAULT_LOCALE, DEFAULT_CLASS_LOADER, this.resourceControl);
        } catch (final MissingResourceException ex) {
            I18N_LOGGER.log(Level.SEVERE, ex, () -> String.format("Failed to load DEFAULT locale bundle for %s!", DEFAULT_LOCALE.getDisplayName()));
            this.defaultTranslations = null;
        }

        try {
            this.internalTranslations = ResourceBundle.getBundle(this.baseName, this.currentLocale, DEFAULT_CLASS_LOADER, this.resourceControl);
        } catch (final MissingResourceException ex) {
            I18N_LOGGER.log(Level.WARNING, ex, () -> String.format("Failed to load internal locale bundle for %s", this.currentLocale.getDisplayName()));
            this.internalTranslations = null;
        }

        if (this.baseDirectory != null && !this.baseDirectory.isEmpty()) {
            if (this.fileResourceLoader == null) {
                this.fileResourceLoader = new FileResourceLoader(DEFAULT_CLASS_LOADER, this.baseDirectory);
            }
            try {
                this.externalTranslations = ResourceBundle.getBundle(this.baseName, this.currentLocale,
                        this.fileResourceLoader, this.resourceControl);
            } catch (final MissingResourceException ex) {
                I18N_LOGGER.log(Level.FINE, ex, () -> String.format("Failed to load internal locale bundle for %s", this.currentLocale.getDisplayName()));
                this.externalTranslations = null;
            }
        } else {
            this.externalTranslations = null;
        }
    }

    /**
     * Gets a map of translation keys to message caches for a specific {@code locale}.
     *
     * @param locale The locale used to retrieve the cache.
     * @return The cache, or a new cache if one did not exist for the locale.
     * @throws NullPointerException if {@code locale} is {@code null}
     * @since 1.0-SNAPSHOT
     */
    private @NotNull HashMap<String, MessageFormat> getMessageCacheForLocale(final @NotNull Locale locale) {
        Objects.requireNonNull(locale);
        return this.formatters.computeIfAbsent(getLocaleString(locale), k -> HashMap.newHashMap(32));
    }

    /**
     * Attempts to translate a {@code key} from a translation file to the translated value.
     *
     * @param  key The translation key. Also used as the fallback translation value.
     * @return     The translated string, or {@code fallback} if translation failed for any reason.
     * @throws NullPointerException if {@code key} is {@code null}
     * @since  1.0-SNAPSHOT
     */
    public @NotNull String translate(final @NotNull String key) {
        return this.translate(key, key, null);
    }

    /**
     * Formats a string from the translation file identified by {@code key}.
     *
     * @param  key      the translation key
     * @param  fallback the fallback translated value
     * @param  format   An array containing objects to use to format the result string.
     * @return The translated string, or {@code fallback} if translation failed for any reason.
     * @throws NullPointerException if {@code key} is {@code null} or {@code fallback} is {@code null}
     * @since 1.0-SNAPSHOT
     */
    public @NotNull String format(final @NotNull String key,
                                  final @NotNull String fallback,
                                  final @Nullable Object... format) {
        return this.translate(key, fallback, format);
    }

    /**
     * Attempts to translate a {@code key} from a translation file to the translated value.
     *
     * @param  key      The translation key.
     * @param  fallback The fallback translation value.
     * @return          The translated string, or {@code fallback} if translation failed for any reason.
     * @throws NullPointerException if {@code key} is {@code null} or if {@code fallback} is {@code null}
     * @since  1.0-SNAPSHOT
     */
    public @NotNull String translate(final @NotNull String key,
                                     final @NotNull String fallback) {
        return this.translate(key, fallback, null);
    }

    /**
     * Attempts to translate a {@code key} from a translation file to the translated value and formats the result with
     * the {@code format} specified.
     *
     * @param  key      The translation key. Also used as the fallback translation value.
     * @param  format   An array containing objects to use to format the result string.
     * @return          The translated string, or {@code fallback} if translation failed for any reason.
     * @throws NullPointerException if {@code key} is {@code null}
     * @since  1.0-SNAPSHOT
     */
    public @NotNull String translate(final @NotNull  String key,
                                     final @Nullable Object[] format) {
        return this.translate(key, key, format);
    }

    /**
     * Attempts to translate a {@code key} from a translation file to the translated value and formats the result with
     * the {@code format} specified.
     *
     * @param  key      The translation key.
     * @param  fallback The fallback translation value.
     * @param  format   An array containing objects to use to format the result string.
     * @return          The translated string, or {@code fallback} if translation failed for any reason.
     * @throws NullPointerException if {@code key} is {@code null} or if {@code fallback} is {@code null}
     * @since  1.0-SNAPSHOT
     */
    public @NotNull String translate(final @NotNull  String key,
                                     final @NotNull  String fallback,
                                     final @Nullable Object[] format) {
        final @NotNull String translatedKey = this.translateRaw(key, fallback);

        if (format == null || format.length == 0) {
            return DOUBLE_APOS.matcher(translatedKey).replaceAll("'");
        } else {
            final @NotNull HashMap<String, MessageFormat> currentLocaleCache = this.getMessageCacheForLocale(this.currentLocale);

            @Nullable MessageFormat formatter = currentLocaleCache.computeIfAbsent(translatedKey, k -> {
                try {
                    return new MessageFormat(k, this.currentLocale);
                } catch (final IllegalArgumentException ex) {
                    I18N_LOGGER.log(Level.WARNING, ex, () -> String.format("Invalid format for translation key \"%s\". Translation arguments will be rejected.", key));
                    // Fallback: try to replace {#} with [#].
                    try {
                        return new MessageFormat(k.replaceAll("\\{(\\D*?)}", "[$1]"), this.currentLocale);
                    } catch (final IllegalArgumentException ignored) { }
                }
                return null;
            });

            @NotNull String result = fallback;
            if (formatter != null) {
                try {
                    result = formatter.format(format);
                } catch (final IllegalArgumentException ignored) {
                    result = formatter.toPattern().replaceAll("\\{(\\d+?)(.*?)}", "[$1]");
                }
            }
            return result;
        }
    }

    /**
     * Translates a {@code key} from a translation file to the translated value. If translation fails for any reason,
     * returns the fallback.
     * @param key      The key to translate.
     * @param fallback The translation fallback.
     * @return         The translated string, or {@code fallback} if translation failed for any reason.
     * @throws NullPointerException if {@code key} is {@code null} or {@code fallback} is {@code null}.
     * @since 1.0-SNAPSHOT
     */
    private @NotNull String translateRaw(final @NotNull String key,
                                         final @NotNull String fallback) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(fallback);
        if (this.externalTranslations != null) {
            try {
                return this.externalTranslations.getString(key);
            } catch (final MissingResourceException ex) {
                I18N_LOGGER.log(Level.FINEST, ex, () -> String.format("Invalid translation key \"%s\" in external locale bundle for %s.", key, this.currentLocale.getDisplayName()));
            }
        }
        if (this.internalTranslations != null) {
            try {
                return this.internalTranslations.getString(key);
            } catch (final MissingResourceException ex) {
                I18N_LOGGER.log(Level.FINE, ex, () -> String.format("Invalid translation key \"%s\" in internal locale bundle for %s.", key, this.currentLocale.getDisplayName()));
            }
        }
        if (this.defaultTranslations != null) {
            try {
                return this.defaultTranslations.getString(key);
            } catch (final MissingResourceException ex) {
                I18N_LOGGER.log(Level.WARNING, ex, () -> String.format("Invalid translation key \"%s\" in DEFAULT locale bundle for %s!", key, DEFAULT_LOCALE.getDisplayName()));
            }
        }
        I18N_LOGGER.log(Level.INFO, String.format("Failed to translate \"%s\" to %s; using fallback value.", key, this.currentLocale.getDisplayName()));
        return fallback;
    }

    //#region Nested and Inner Classes

    /**
     * Defines types of resource files that the {@link I18n} instance might use.
     * <p>
     * Also allows for a custom resource file handler to be set by way of the {@code CUSTOM} option. See
     * {@link I18n#setResourceType(ResourceType, Control)} for more information on how to use this feature.
     *
     * @author Isabel Maskrey
     * @since 1.0-SNAPSHOT
     */
    public enum ResourceType {
        /**
         * Load resources from *.lang files.
         * <p>
         * *.lang files are Java properties formatted files containing translation keys matched to optionally formatted
         * values. These files are provided by implementers in their JAR files. By default, these files are matched
         * using an instance of {@link LanguageResourceBundle.Control}, which loads files from a base package specified
         * by the {@code baseName} which match the naming format &lt;language&gt;_&lt;countryCode&gt;.lang.
         */
        LANG,
        /**
         * Load resources from a *.json file.
         * <p>
         * The translation keys will be loaded from a properly formatted JSON file using the
         * {@link JsonResourceBundle.Control} resource bundle controller. JSON files are loaded from the package or
         * folder specified by the {@code baseName} which match the format &lt;language&gt;_&lt;countryCode&gt;.json.
         */
        JSON,
        /**
         * Load resources with a custom resource control.
         * <p>
         * Only works with {@link I18n#setResourceType(ResourceType, Control)}, but allows implementers to set a
         * specifically-configured {@link ResourceBundle.Control} to load files to whatever specification they would
         * like.
         */
        CUSTOM
    }

    //#endregion
}
