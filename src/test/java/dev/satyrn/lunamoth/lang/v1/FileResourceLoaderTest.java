package dev.satyrn.lunamoth.lang.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the {@code FileResourceLoader} class.
 *
 * @author Isabel Maskrey
 * @since  1.0-SNAPSHOT
 */
public class FileResourceLoaderTest {

    /**
     * Creates a new instance of this test class.
     *
     * @since 1.0-SNAPSHOT
     */
    public FileResourceLoaderTest() { }

    /**
     * Tests the {@code loadClass} method.
     * <p>
     * {@code loadClass} should always fail with an {@link UnsupportedOperationException}.
     *
     * @since 1.0-SNAPSHOT
     */
    @Test
    void testLoadClass() {
        final @NotNull FileResourceLoader loader = new FileResourceLoader(this.getClass().getClassLoader(), "testFiles");
        // Load class should always throw an exception
        assertThrows(UnsupportedOperationException.class, () -> loader.loadClass("dev.satyrn.lunamoth.lang.v1.FileResourceLoader"));
    }

    /**
     * Tests the {@code getResource} method.
     *
     * @since 1.0-SNAPSHOT
     */
    @Test
    void testGetResource() throws URISyntaxException {
        final @NotNull FileResourceLoader loader = new FileResourceLoader(this.getClass().getClassLoader(), "testFiles");
        final @Nullable URL resourceUrl = loader.getResource("lang/en_us.lang");
        assertNotNull(resourceUrl);
        final @NotNull File getFile = new File(resourceUrl.toURI());
        assertTrue(getFile.exists());

        final @Nullable URL missingUrl = loader.getResource("lang/nonexistant.json");
        assertNull(missingUrl);
    }

    /**
     * Tests the {@code getResourceAsStream} method.
     *
     * @since 1.0-SNAPSHOT
     */
    @Test
    void testGetResourceAsStream() throws IOException {
        try (final InputStream stream = new FileResourceLoader(this.getClass().getClassLoader(), "testFiles").getResourceAsStream("lang/en_us.lang")) {
            assertNotNull(stream);
        }
    }
}