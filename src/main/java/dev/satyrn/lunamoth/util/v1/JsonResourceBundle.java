package dev.satyrn.lunamoth.util.v1;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Represents a {@link ResourceBundle} that uses a JSON object as its underlying data source.
 * <p>
 * This class allows for accessing JSON properties as {@code ResourceBundle} entries.
 *
 * @author Isabel Maskrey
 * @since 1.0-SNAPSHOT
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
     * Creates a new resource bundle to handle JSON files. The input file is read with the UTF-8 standard charset.
     *
     * @param  stream The input stream.
     * @throws IOException If any error occurs during I/O operations.
     * @since  1.0-SNAPSHOT
     */
    public JsonResourceBundle(final @NotNull InputStream stream) throws IOException {
        try (final @NotNull InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            this.json = JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    /**
     * Creates a new resource bundle to handle JSON files. Unlike the constructor
     * {@link #JsonResourceBundle(InputStream)}, there are no limitations on the character set used to read the files.
     *
     * @param  reader The input stream reader.
     * @since  1.0-SNAPSHOT
     */
    @SuppressWarnings({"unused"})
    public JsonResourceBundle(final @NotNull InputStreamReader reader) {
        this.json = JsonParser.parseReader(reader).getAsJsonObject();
    }

    /**
     * Gets an item from the underlying JSON object identified by {@code key}.
     * <p>
     * The object identified by the string must be a JSON primitive, or a single JSON primitive in an array. Otherwise,
     * the function returns {@code null}.
     *
     * @param  key the key for the desired object
     * @return the value identified by the key as a {@code String} if it is a JSON primitive or a single JSON primitive
     *         in an array.
     * @since  1.0-SNAPSHOT
     */
    @Override
    protected @Nullable Object handleGetObject(@NotNull String key) {
        final @Nullable JsonElement object = this.json.get(key);
        if (object != null) {
            if (object.isJsonPrimitive()) {
                return object.getAsString();
            }
            if (object instanceof JsonArray jsonArray && jsonArray.asList().size() == 1) {
                final @Nullable JsonElement firstElement = jsonArray.get(0);
                if (firstElement != null && firstElement.isJsonPrimitive()) {
                    return jsonArray.getAsString();
                }
            }
        }
        return null;
    }

    /**
     * Gets the keys present in this resource.
     *
     * @return The key set.
     * @since 1.0-SNAPSHOT
     */
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
     * @since 1.0-SNAPSHOT
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
         * @since 1.0-SNAPSHOT
         */
        public Control() {
            super("json");
        }

        /**
         * Gets a new resource bundle of the type handled by this control file.
         *
         * @param  stream The resource stream for the resource.
         * @return The new resource bundle from the stream.
         * @throws IOException if an error occurred when reading resources using any I/O operation.
         * @since 1.0-SNAPSHOT
         */
        @Override
        protected @NotNull JsonResourceBundle newBundle(final @NotNull InputStream stream) throws IOException {
            return new JsonResourceBundle(stream);
        }
    }
}
