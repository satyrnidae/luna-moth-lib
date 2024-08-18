package dev.satyrn.lunamoth.util.function.v1;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests the {@link TriFunction} functional interface.
 * @since 1.0.0-SNAPSHOT
 */
public class TriFunctionTest {
    /**
     * Applies a tri-function in which the three parameters are added together.
     */
    @Test
    void testApply() {
        TriFunction<Integer, Integer, Integer, Integer> sum = (a, b, c) -> a + b + c;
        assertEquals(6, sum.apply(1, 2, 3), "Sum of 1, 2, 3 should be 6");
    }

    /**
     * Tests composing a multiple-step function with TriFunction
     */
    @Test
    void testAndThen() {
        TriFunction<Integer, Integer, Integer, Integer> sum = (a, b, c) -> a + b + c;
        Function<Integer, String> intToString = Object::toString;

        TriFunction<Integer, Integer, Integer, String> sumThenConvert = sum.andThen(intToString);
        assertEquals("6", sumThenConvert.apply(1, 2, 3), "Sum of 1, 2, 3 should be converted to '6'");
    }

    /**
     * Ensures the composure fails with an {@link IllegalArgumentException} if the composed function is null.
     */
    @Test
    @SuppressWarnings({"ConstantConditions"})
    void testAndThenNullFunction() {
        TriFunction<Integer, Integer, Integer, Integer> sumFunction = (a, b, c) -> a + b + c;
        assertThrows(IllegalArgumentException.class, () -> sumFunction.andThen(null));
    }
}
