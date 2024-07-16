package dev.satyrn.lunamoth.util.v1;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Represents a {@link ResourceBundle} that uses a JSON object as its underlying data source.
 * <p>
 * This class allows for accessing JSON properties as {@code ResourceBundle} entries.
 * 
 * @author Isabel Maskrey
 * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
 */
// TODO: Extract to Moon Moth common library
public class JsonResourceBundle extends ResourceBundle {
    /**
     * The underlying JSON object.
     * 
     * @since 3.0.0-papermc-api.1.12-R0.1-SNAPSHOT
     */
    private final @NotNull JsonObject json;

    /**
     *
     * @param stream
     * @throws IOException
     */
    public JsonResourceBundle(final @NotNull InputStream stream) throws IOException {
        try (final @NotNull InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            this.json = JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    /**
     *
     * @param reader
     * @throws IOException
     */
    public JsonResourceBundle(final @NotNull InputStreamReader reader) throws IOException {
        this.json = JsonParser.parseReader(reader).getAsJsonObject();
    }

    @Override
    protected @Nullable Object handleGetObject(@NotNull String key) {
        // TODO: weird recursive shit I can't do right now.
        //       I want to be able to unwrap JSON objects because
        //       I Love Torturing Myself with Recursive Functions o(*////▽////*)q

        // for now just return primitives / arrays as strings
        @Nullable JsonElement value = json.get(key);
        if (value != null) {
            if (value.isJsonPrimitive() || value.isJsonArray()) {
                return value.getAsString();
            }
        }
        return null;
    }

    @Override
    public @NotNull Enumeration<String> getKeys() {
        return Collections.enumeration(json.keySet());
    }

    /**
     * Used in the factory method {@link ResourceBundle#getBundle(String, Locale, ClassLoader, ResourceBundle.Control)}
     * to get a new instance of {@link JsonResourceBundle}.
     * <p>
     * See {@link ResourceBundle.Control} for more details.
     *
     * @author Isabel Maskrey
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final static class Control extends AbstractResourceFileControl<JsonResourceBundle> {
        /**
         * Represents a resource bundle loader that handles JSON-formatted files.
         * <p>
         * In particular, {@link #toBundleName(String, Locale)} is overriden to return a bundle name in the format
         * &lt;language&gt;_&lt;country code&gt; such that the locale {@link Locale#US} would be formatted as
         * {@code en_us}.
         * <p>
         * Additionally, resource files are loaded by a custom resource name with a specific file extension, in this case,
         * {@code "json"}.
         *
         * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
         */
        public Control() {
            super("json");
        }

        /**
         * Gets a new resource bundle of the type handled by this control file.
         *
         * @param stream The resource stream for the resource.
         * @return       The new resource bundle from the stream.
         * @throws IOException if an error occurred when reading resources using any I/O operation.
         * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
         */
        @Override
        protected @NotNull JsonResourceBundle newBundle(final @NotNull InputStream stream) throws IOException {
            return new JsonResourceBundle(stream);
        }
    }
}
