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
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class JsonResourceBundleControlTest {
    @Mock
    private InputStream inputStream;

    private JsonResourceBundle.Control control;

    @BeforeEach
    void setUp() {
        control = new JsonResourceBundle.Control();
    }

    @Test
    void testToBundleName() {
        String baseName = "baseName";
        Locale locale = Locale.of("en", "US");
        String bundleName = control.toBundleName(baseName, locale);
        assertEquals("baseName.en_us", bundleName);
    }

    @Test
    void testNewBundle() throws IOException {
        String jsonContent = "{\"key1\": \"value1\", \"key2\": \"value2\"}";
        InputStream jsonStream = new ByteArrayInputStream(jsonContent.getBytes());

        JsonResourceBundle bundle = control.newBundle(jsonStream);
        assertNotNull(bundle);
    }

    @Test
    void testNewBundle_JsonIOException() throws IOException {
        doThrow(new IOException("Test Exception")).when(inputStream).read(any(byte[].class), anyInt(), anyInt());
        assertThrows(JsonIOException.class, () -> control.newBundle(inputStream));
    }
}
