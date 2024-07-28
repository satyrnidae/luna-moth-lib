package dev.satyrn.lunamoth.util.v1;

import com.google.gson.JsonIOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Enumeration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JsonResourceBundleTest {
    @Mock
    private InputStream inputStream;

    private JsonResourceBundle jsonResourceBundle;

    @BeforeEach
    void setUp() throws IOException {
        String jsonContent = "{\"key1\": \"value1\", \"key2\": \"value2\", \"key3\": [\"value3\"]}";
        InputStream jsonStream = new ByteArrayInputStream(jsonContent.getBytes());
        jsonResourceBundle = new JsonResourceBundle(jsonStream);
    }

    @Test
    void testHandleGetObject_ValidKey() {
        assertEquals("value1", jsonResourceBundle.handleGetObject("key1"));
        assertEquals("value2", jsonResourceBundle.handleGetObject("key2"));
    }

    @Test
    void testHandleGetObject_ArrayKey() {
        assertEquals("value3", jsonResourceBundle.handleGetObject("key3"));
    }

    @Test
    void testHandleGetObject_InvalidKey() {
        assertNull(jsonResourceBundle.handleGetObject("invalidKey"));
    }

    @Test
    void testGetKeys() {
        Enumeration<String> keys = jsonResourceBundle.getKeys();
        assertNotNull(keys);

        // Convert Enumeration to a List for easier assertion
        java.util.List<String> keyList = Collections.list(keys);
        assertEquals(3, keyList.size());
        assertTrue(keyList.contains("key1"));
        assertTrue(keyList.contains("key2"));
        assertTrue(keyList.contains("key3"));
    }

    @Test
    void testConstructor_WithInputStream_JsonIOException() throws IOException {
        // Mock InputStream to throw IOException when constructing InputStreamReader
        try (InputStream mockInputStream = mock(InputStream.class)) {
            doThrow(new IOException("Mock IOException")).when(mockInputStream).read(any(byte[].class), anyInt(), anyInt());

            JsonIOException exception = assertThrows(JsonIOException.class, () -> new JsonResourceBundle(mockInputStream));
            assertEquals("java.io.IOException: Mock IOException", exception.getMessage());
        }
    }

    @Test
    void testConstructor_JsonIOException() {
        JsonIOException exception = assertThrows(JsonIOException.class, () -> new JsonResourceBundle(inputStream));
        assertEquals("java.io.IOException: Underlying input stream returned zero bytes", exception.getMessage());
    }

    @Test
    void testConstructor_WithInputStreamReader_JsonIOException() throws IOException {
        InputStreamReader reader = mock(InputStreamReader.class);
        doThrow(new IOException("Mock IOException")).when(reader).read(any(char[].class), anyInt(), anyInt());

        JsonIOException exception = assertThrows(JsonIOException.class, () -> new JsonResourceBundle(reader));
        assertEquals("java.io.IOException: Mock IOException", exception.getMessage());
    }
}