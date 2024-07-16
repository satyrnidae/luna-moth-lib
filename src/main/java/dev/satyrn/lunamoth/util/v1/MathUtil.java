package dev.satyrn.lunamoth.util.v1;

import org.jetbrains.annotations.Contract;

/**
 * Provides common math functionality.
 *
 * @author Isabel Maskrey
 * @since 1.0-SNAPSHOT
 */
public final class MathUtil {
    /**
     * Constructs a new instance of {@code MathUtil}.
     * <p>
     * Note: This constructor is private to prevent instantiation of {@code MathUtil}, as it is a static utility class.
     * Attempts to instantiate this class will result in a {@link UnsupportedOperationException} being thrown.
     *
     * @throws UnsupportedOperationException if an attempt is made to instantiate {@code MathUtil}.
     * @since 1.0-SNAPSHOT
     */
    @Contract(value = "-> fail", pure = true)
    MathUtil() {
        throw new UnsupportedOperationException("MathUtil cannot be instantiated.");
    }

    /**
     * Returns the value of {@code d} clamped to the range [min, max].
     *
     * @param d   the value to be clamped
     * @param min the minimum value in the range
     * @param max the maximum value in the range
     * @return the clamped value {@code d}, which is {@code min} if {@code d} is less than {@code min},
     *         {@code max} if {@code d} is greater than {@code max}, or {@code d} itself if {@code min <= d <= max}
     * @throws IllegalArgumentException if min is greater than max
     * @since 1.0-SNAPSHOT
     */
    @Contract(pure = true)
    public static double clamp(final double d, final double min, final double max) {
        if (min > max) {
            throw new IllegalArgumentException("min cannot be greater than max");
        }
        return Math.max(min, Math.min(d, max));
    }

    /**
     * Returns the value of {@code f} clamped to the range [min, max].
     *
     * @param f   the value to be clamped
     * @param min the minimum value in the range
     * @param max the maximum value in the range
     * @return the clamped value {@code f}, which is {@code min} if {@code f} is less than {@code min},
     *         {@code max} if {@code f} is greater than {@code max}, or {@code f} itself if {@code min <= f <= max}
     * @throws IllegalArgumentException if min is greater than max
     * @since 1.0-SNAPSHOT
     */
    @Contract(pure = true)
    public static float clamp(final float f, final float min, final float max) {
        if (min > max) {
            throw new IllegalArgumentException("min cannot be greater than max");
        }
        return Math.max(min, Math.min(f, max));
    }

    /**
     * Returns the value of {@code i} clamped to the range [min, max].
     *
     * @param i   the value to be clamped
     * @param min the minimum value in the range
     * @param max the maximum value in the range
     * @return the clamped value {@code i}, which is {@code min} if {@code i} is less than {@code min},
     *         {@code max} if {@code i} is greater than {@code max}, or {@code i} itself if {@code min <= i <= max}
     * @throws IllegalArgumentException if min is greater than max
     * @since 1.0-SNAPSHOT
     */
    @Contract(pure = true)
    public static int clamp(final int i, final int min, final int max) {
        if (min > max) {
            throw new IllegalArgumentException("min cannot be greater than max");
        }
        return Math.max(min, Math.min(i, max));
    }

    /**
     * Computes the logarithm of a specified base for a given value.
     *
     * @param value the value whose logarithm is to be computed
     * @param base the base of the logarithm
     * @return the logarithm of {@code value} with respect to the specified {@code base}
     * @throws IllegalArgumentException if {@code value} or {@code base} is non-positive,
     *                                  or if {@code base} is 1
     * @since 1.0-SNAPSHOT
     */
    @Contract(pure = true)
    public static double log(final double value, final double base) {
        if (value <= 0 || base <= 0) {
            throw new IllegalArgumentException("both value and base must be positive");
        }
        if (base == 1) {
            throw new IllegalArgumentException("logarithm for base 1 is undefined");
        }
        return Math.log10(value) / Math.log10(base);
    }
}
