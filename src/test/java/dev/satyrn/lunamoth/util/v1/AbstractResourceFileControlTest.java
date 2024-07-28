package dev.satyrn.lunamoth.util.v1;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbstractResourceFileControlTest {

    private static class TestResourceBundle extends ResourceBundle {
        private final Map<String, String> data;

        public TestResourceBundle(Map<String, String> data) {
            this.data = data;
        }

        @Override
        protected Object handleGetObject(@NotNull String key) {
            return data.get(key);
        }

        @Override
        public @NotNull Enumeration<String> getKeys() {
            return Collections.enumeration(data.keySet());
        }
    }

    private static class TestResourceFileControl extends AbstractResourceFileControl<TestResourceBundle> {
        public TestResourceFileControl() {
            super("test");
        }

        @Override
        protected @NotNull TestResourceBundle newBundle(@NotNull InputStream stream) {
            // Assume the stream contains a simple key=value format
            Scanner scanner = new Scanner(stream);
            Map<String, String> data = new HashMap<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    data.put(parts[0], parts[1]);
                }
            }
            return new TestResourceBundle(data);
        }
    }

    @Mock
    private ClassLoader classLoader;

    @Mock
    private URL url;

    @Mock
    private URLConnection urlConnection;

    private TestResourceFileControl control;

    @BeforeEach
    void setUp() {
        control = new TestResourceFileControl();
    }

    @Test
    void testToResourceName() {
        String baseName = "base.name";
        Locale locale = Locale.US;
        String expectedResourceName = "base/name/en_us.test";
        String resourceName = control.toResourceName(baseName, locale);
        assertEquals(expectedResourceName, resourceName);
    }

    @Test
    void testToBundleName() {
        String baseName = "base.name";
        Locale locale = Locale.US;
        String expectedBundleName = "base.name.en_us";
        String bundleName = control.toBundleName(baseName, locale);
        assertEquals(expectedBundleName, bundleName);
    }

    @Test
    void testNewBundle_ValidStream() throws IOException {
        String baseName = "base.name";
        Locale locale = Locale.US;
        String format = "test";
        boolean reload = false;
        String resourceContent = "key1=value1\nkey2=value2";
        InputStream stream = new ByteArrayInputStream(resourceContent.getBytes());

        when(classLoader.getResourceAsStream(anyString())).thenReturn(stream);

        ResourceBundle bundle = control.newBundle(baseName, locale, format, classLoader, reload);

        assertNotNull(bundle);
        assertEquals("value1", bundle.getString("key1"));
        assertEquals("value2", bundle.getString("key2"));
    }

    @Test
    void testNewBundle_MissingResourceException() {
        String baseName = "base.name";
        Locale locale = Locale.US;
        String format = "test";
        boolean reload = false;

        when(classLoader.getResourceAsStream(anyString())).thenReturn(null);

        assertThrows(MissingResourceException.class, () -> control.newBundle(baseName, locale, format, classLoader, reload));
    }

    @Test
    void testGetReloadedResourceAsStream() throws IOException {
        String resourceName = "resource.name";

        when(classLoader.getResource(resourceName)).thenReturn(url);
        when(url.openConnection()).thenReturn(urlConnection);
        when(urlConnection.getInputStream()).thenReturn(new ByteArrayInputStream("content".getBytes()));

        InputStream stream = control.getReloadedResourceAsStream(classLoader, resourceName);
        assertNotNull(stream);
    }

    @Test
    void testGetReloadedResourceAsStream_IOException() throws IOException {
        String resourceName = "resource.name";

        when(classLoader.getResource(resourceName)).thenReturn(url);
        when(url.openConnection()).thenReturn(urlConnection);
        when(urlConnection.getInputStream()).thenThrow(new IOException("Mock IOException"));

        assertThrows(IOException.class, () -> control.getReloadedResourceAsStream(classLoader, resourceName));
    }

    @Test
    void testGetReloadedResourceAsStream_Null() throws IOException {
        String resourceName = "resource.name";

        when(classLoader.getResource(resourceName)).thenReturn(null);

        InputStream stream = control.getReloadedResourceAsStream(classLoader, resourceName);
        assertNull(stream);
    }
}