package dev.satyrn.lunamoth.util.v1;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Parameters {

    @Contract(value = "_, null -> fail; _, !null -> _")
    public static void requireNonNull(final @NotNull String paramName, final @Nullable Object value) {
        if (value == null) {
            throw new IllegalArgumentException(paramName + " cannot be null");
        }
    }

    public static void requireInBounds(final @NotNull String paramName,
                                       final @NotNull Number value,
                                       final @NotNull Number lowerBoundInclusive,
                                       final @NotNull Number upperBoundInclusive) {
        if (value.doubleValue() < lowerBoundInclusive.doubleValue()
            || value.doubleValue() > upperBoundInclusive.doubleValue()) {
            throw new IllegalArgumentException(paramName + " must be within the range [" + lowerBoundInclusive + "," + upperBoundInclusive + "]");
        }
    }
}
