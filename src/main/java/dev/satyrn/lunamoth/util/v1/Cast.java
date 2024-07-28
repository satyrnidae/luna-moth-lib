package dev.satyrn.lunamoth.util.v1;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Utility for inlining certain cast operations between objects.
 *
 * @author  Isabel Maskrey
 * @since   1.0-SNAPSHOT
 * @apiNote Ditched the unchecked cast {@code to(Object)} function since ClassCastException is thrown in a weird place
 *          due to type erasure, and it was not recommended to cast that way anyways.
 */
public final class Cast {
    /**
     * Private constructor for {@code Cast} to prevent instantiation.
     * <p>
     * This constructor throws an {@link UnsupportedOperationException} with a message indicating that
     * {@code Cast} cannot be instantiated.
     *
     * @throws  UnsupportedOperationException if an attempt is made to instantiate {@code Cast}
     * @apiNote This constructor will always throw an exception, ensuring that instances of {@code Cast} cannot be
     *          created.
     * @since   1.0-SNAPSHOT
     */
    @Contract(value = "-> fail", pure = true)
    Cast() {
        throw new UnsupportedOperationException("Cast cannot be instantiated.");
    }

    /**
     * Converts an {@code object} to an {@link Optional} of type {@code T}.
     * <p>
     * If {@code object} is {@code null}, returns an empty {@link Optional}.
     * <p>
     * If {@code object} can be cast to {@code targetClass}, returns an {@link Optional} containing the cast object.
     * <p>
     * If {@code targetClass} is {@link String}, converts {@code object} to {@link String} via {@link Object#toString()}
     * and returns it as an {@link Optional}.
     *
     * @param  <T>         the type of the target class
     * @param  targetClass the target class to which {@code object} should be converted
     * @param  object      the object to convert
     * @return an {@link Optional} containing the converted object if conversion is successful, otherwise an empty
     *         {@link Optional}
     * @throws NullPointerException if {@code targetClass} is {@code null}.
     * @since  1.0-SNAPSHOT
     */
    @Contract(value = "_, _ -> !null")
    public static <T> @NotNull Optional<T> to(final @NotNull Class<T> targetClass,
                                              final @Nullable Object object) {
        Objects.requireNonNull(targetClass);
        if (object == null) return Optional.empty();

        T cast = null;

        if (targetClass.isAssignableFrom(object.getClass())) {
            cast = targetClass.cast(object);
        } else if (targetClass == String.class) {
            cast = targetClass.cast(object.toString());
        }

        return Optional.ofNullable(cast);
    }

    /**
     * Attempts to cast an {@code object} to type {@code T} using {@link #to(Class, Object)}. If the cast fails or the
     * {@code object} is {@code null}, it returns a default value provided by the {@code supplier}.
     *
     * @param  <T>         the type of the target class
     * @param  targetClass the target class to which {@code object} should be converted
     * @param  object      the object to convert
     * @param  supplier    the supplier to provide a default value if the conversion is not successful
     * @return the converted object if conversion is successful, otherwise the value provided by
     *         {@code supplier}
     * @throws NullPointerException if {@code targetClass} is {@code null} or {@code supplier} is {@code null}
     * @since  1.0-SNAPSHOT
     * @see    #to(Class, Object)
     */
    @Contract(value = "_, _, _ -> _")
    @SuppressWarnings({"unused"})
    public static <T> @Nullable T orElseGet(final @NotNull Class<T> targetClass,
                                            final @Nullable Object object,
                                            final @NotNull Supplier<@Nullable T> supplier) {
        Objects.requireNonNull(supplier);
        return to(targetClass, object).orElseGet(supplier);
    }

    /**
     * Attempts to cast an {@code object} to type {@code T} using {@link #to(Class, Object)}.
     * <p>
     * If the cast fails or the {@code object} is {@code null}, it returns {@code null}.
     *
     * @param  <T>         the type of the target class
     * @param  targetClass the target class to which {@code object} should be converted
     * @param  object      the object to convert
     * @return the converted object if conversion is successful, otherwise {@code null}
     * @throws NullPointerException if {@code targetClass} is {@code null}.
     * @since  1.0-SNAPSHOT
     * @see    #orElse(Class, Object, Object)
     */
    @Contract("_, _ -> _")
    @SuppressWarnings({"unused"})
    public static <T> @Nullable T orNull(final @NotNull Class<T> targetClass,
                                         final @Nullable Object object) {
        return orElse(targetClass, object, null);
    }

    /**
     * Attempts to cast an {@code object} to type {@code T} using {@link #to(Class, Object)}.
     * <p>
     * If the cast fails or the {@code object} is {@code null}, it returns the provided default value {@code other}.
     *
     * @param  <T>         the type of the target class
     * @param  targetClass the target class to which {@code object} should be converted
     * @param  object      the object to convert
     * @param  other       the default value to return if the conversion is not successful
     * @return the converted object if conversion is successful, otherwise the provided default value {@code other}
     * @throws NullPointerException if {@code targetClass} is {@code null}
     * @since  1.0-SNAPSHOT
     * @see    #to(Class, Object)
     */
    @Contract("_, _, _ -> _")
    public static <T> @Nullable T orElse(final @NotNull Class<T> targetClass,
                                         final @Nullable Object object,
                                         final @Nullable T other) {
        return to(targetClass, object).orElse(other);
    }

    /**
     * Attempts to cast an {@code object} to {@code String} using {@link #to(Class, Object)}.
     * @param object the object to convert.
     * @return If {@code object} is already a {@code String}, then returns the object. If {@code object} is
     *         {@code null}, returns {@code null}. If {@code object} is not a {@code String} or {@code null}, returns
     *         the string representation of the object by calling its {@code toString()} method.
     * @since 1.0-SNAPSHOT
     * @see #to(Class, Object)
     */
    public static @Nullable String toString(final @Nullable Object object) {
        return orNull(String.class, object);
    }
}
