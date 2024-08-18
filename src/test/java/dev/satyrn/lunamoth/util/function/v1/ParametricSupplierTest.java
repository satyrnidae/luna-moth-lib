package dev.satyrn.lunamoth.util.function.v1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link ParametricSupplier} interface.
 *
 * @since 1.0.0-SNAPSHOT
 */
public class ParametricSupplierTest {
    /**
     * A simple implementation of {@link ParametricSupplier} for testing purposes.
     */
    private static class TestParametricSupplier implements ParametricSupplier<String, Integer> {
        @Override
        public Integer get(String s) {
            if (s == null) {
                throw new IllegalArgumentException("Input cannot be null");
            }
            return s.length();
        }
    }

    /**
     * Tests the {@link ParametricSupplier#get(Object)} method.
     */
    @Test
    void testGet() {
        ParametricSupplier<String, Integer> supplier = new TestParametricSupplier();

        // Test with a non-null value
        assertEquals(5, supplier.get("Hello"), "The length of 'Hello' should be 5");

        // Test with an empty string
        assertEquals(0, supplier.get(""), "The length of an empty string should be 0");

        // Test with a null value
        assertThrows(IllegalArgumentException.class, () -> supplier.get(null),
                "get() should throw IllegalArgumentException for null input");
    }

    /**
     * Tests the {@link ParametricSupplier#apply(Object)} method.
     */
    @Test
    void testApply() {
        ParametricSupplier<String, Integer> supplier = new TestParametricSupplier();

        // Test with a non-null value
        assertEquals(5, supplier.apply("Hello"), "The length of 'Hello' should be 5");

        // Test with an empty string
        assertEquals(0, supplier.apply(""), "The length of an empty string should be 0");

        // Test with a null value
        assertThrows(IllegalArgumentException.class, () -> supplier.apply(null),
                "apply() should throw IllegalArgumentException for null input");
    }
}
