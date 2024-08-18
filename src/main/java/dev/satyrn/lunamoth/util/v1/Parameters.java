package dev.satyrn.lunamoth.util.v1;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

/**
 * Utility class for parameter validation. Provides static methods to ensure
 * that parameters meet specific criteria and throw exceptions if they do not.
 *
 * @author Isabel Maskrey
 * @since 1.0.0-SNAPSHOT
 */
public class Parameters {

    /**
     * Ensures that the specified value is not {@code null}. If the value is {@code null},
     * an {@code IllegalArgumentException} is thrown with a message that includes the
     * specified parameter name.
     *
     * @param paramName the name of the parameter to include in the exception message
     * @param value     the value to check for {@code null}
     * @throws IllegalArgumentException if the value is {@code null}
     * @since 1.0.0-SNAPSHOT
     */
    @Contract(value = "_, null -> fail; _, !null -> _")
    public static void requireNonNull(final @NotNull String paramName, final @Nullable Object value) {
        if (value == null) {
            throw new IllegalArgumentException(paramName + " cannot be null");
        }
    }

    /**
     * Ensures that the specified numeric value is within the inclusive range defined by
     * the lower and upper bounds. If the value is outside of this range, an
     * {@code IllegalArgumentException} is thrown with a message that includes the
     * specified parameter name and the range.
     *
     * @param paramName         the name of the parameter to include in the exception message
     * @param value             the value to check
     * @param lowerBoundInclusive the lower bound of the valid range (inclusive)
     * @param upperBoundInclusive the upper bound of the valid range (inclusive)
     * @throws IllegalArgumentException if the value is outside the specified range
     * @since 1.0.0-SNAPSHOT
     */
    public static void requireInBounds(final @NotNull String paramName,
                                       final @NotNull Number value,
                                       final @NotNull Number lowerBoundInclusive,
                                       final @NotNull Number upperBoundInclusive) {
        if (value.doubleValue() < lowerBoundInclusive.doubleValue()
            || value.doubleValue() > upperBoundInclusive.doubleValue()) {
            throw new IllegalArgumentException(paramName + " must be within the range [" + lowerBoundInclusive + "," + upperBoundInclusive + "]");
        }
    }

    /**
     * Ensures that the specified array is not {@code null} and does not contain any {@code null}
     * elements. If the array is {@code null} or contains {@code null} elements, an
     * {@code IllegalArgumentException} is thrown with a message that includes the
     * specified parameter name.
     *
     * @param paramName the name of the parameter to include in the exception message
     * @param value     the array to check
     * @param <T>       the type of the array elements
     * @throws IllegalArgumentException if the array is {@code null} or contains {@code null} elements
     * @since 1.0.0-SNAPSHOT
     */
    public static <T> void requireAllNonNull(final @NotNull String paramName, final @Nullable T[] value) {
        if (value == null || Arrays.stream(value).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(paramName + " cannot be null or contain null values");
        }
    }
}
