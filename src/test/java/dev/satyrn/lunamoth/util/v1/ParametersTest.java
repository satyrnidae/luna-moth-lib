package dev.satyrn.lunamoth.util.v1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests the {@link Parameters} class.
 *
 * @since 1.0.0-SNAPSHOT
 */
public class ParametersTest {
    /**
     * Make sure no exception is thrown if the parameter value is not null.
     */
    @Test
    public void testRequireNonNull_WithNonNullValue_DoesNotThrow() {
        // Should not throw
        Parameters.requireNonNull("param", new Object());
    }

    /**
     * Make sure an exception is thrown when the parameter value is null.
     */
    @Test
    public void testRequireNonNull_WithNullValue_ThrowsException() {
        // Should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> Parameters.requireNonNull("param", null));
    }

    /**
     * Make sure an exception is not thrown when the parameter value is in bounds.
     */
    @Test
    public void testRequireInBounds_WithValueInRange_DoesNotThrow() {
        // Should not throw
        Parameters.requireInBounds("param", 5, 0, 10);
    }

    /**
     * Make sure an exception is thrown when the parameter value is below the allowed range.
     */
    @Test
    public void testRequireInBounds_WithValueBelowRange_ThrowsException() {
        // Should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> Parameters.requireInBounds("param", -1, 0, 10));
    }

    /**
     * Make sure an exception is thrown when the parameter value is above the allowed range.
     */
    @Test
    public void testRequireInBounds_WithValueAboveRange_ThrowsException() {
        // Should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> Parameters.requireInBounds("param", 11, 0, 10));
    }

    /**
     * Make sure no exception is thrown if there are no null values in the parameter array.
     */
    @Test
    public void testRequireAllNonNull_WithAllNonNullValues_DoesNotThrow() {
        // Should not throw
        Parameters.requireAllNonNull("param", new Object[]{new Object(), new Object()});
    }

    /**
     * Make sure that an exception is thrown if any values in the parameter array are null.
     */
    @Test
    public void testRequireAllNonNull_WithNullInArray_ThrowsException() {
        // Should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> Parameters.requireAllNonNull("param", new Object[]{new Object(), null}));
    }

    /**
     * Make sure that an exception is thrown if the parameter array itself is null.
     */
    @Test
    public void testRequireAllNonNull_WithNullArray_ThrowsException() {
        // Should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> Parameters.requireAllNonNull("param", null));
    }
}
