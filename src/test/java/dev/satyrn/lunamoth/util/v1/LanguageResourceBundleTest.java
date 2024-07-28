package dev.satyrn.lunamoth.util.v1;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class LanguageResourceBundleTest {

    @Test
    void testLanguageResourceBundleInputStream() throws IOException {
        String propertiesContent = "key1=value1\nkey2=value2";
        InputStream stream = new ByteArrayInputStream(propertiesContent.getBytes(StandardCharsets.UTF_8));
        LanguageResourceBundle resourceBundle = new LanguageResourceBundle(stream);

        assertEquals("value1", resourceBundle.getString("key1"));
        assertEquals("value2", resourceBundle.getString("key2"));
    }

    @Test
    void testLanguageResourceBundleInputStreamReader() throws IOException {
        String propertiesContent = "key1=value1\nkey2=value2";
        InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(propertiesContent.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        LanguageResourceBundle resourceBundle = new LanguageResourceBundle(reader);

        assertEquals("value1", resourceBundle.getString("key1"));
        assertEquals("value2", resourceBundle.getString("key2"));
    }

    @Test
    void testLanguageResourceBundleEmptyStream() throws IOException {
        InputStream emptyStream = new ByteArrayInputStream(new byte[0]);
        LanguageResourceBundle resourceBundle = new LanguageResourceBundle(emptyStream);

        assertNotNull(resourceBundle);
    }

    @Test
    void testLanguageResourceBundleIOException() {
        // Create a mocked InputStream
        InputStream mockedInputStream = mock(InputStream.class);

        // Make the InputStream throw an IOException when read(byte[], int, int) is called
        try {
            doThrow(new IOException("Mocked IOException")).when(mockedInputStream).read(any(byte[].class), anyInt(), anyInt());
        } catch (IOException e) {
            // This shouldn't happen in setting up the mock
            e.printStackTrace();
        }

        // Assert that the LanguageResourceBundle constructor throws an IOException
        assertThrows(IOException.class, () -> new LanguageResourceBundle(mockedInputStream));
    }
}