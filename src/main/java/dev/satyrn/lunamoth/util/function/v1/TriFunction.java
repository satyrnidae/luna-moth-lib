package dev.satyrn.lunamoth.util.function.v1;

import dev.satyrn.lunamoth.util.v1.Parameters;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Represents a function that accepts three arguments and produces a result.
 * <p>
 * This functional interface is similar to {@link java.util.function.BiFunction}, but it takes three input arguments
 * instead of two. It also provides a default method for composing this function with another function, which allows
 * chaining operations.
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <V> the type of the third argument to the function
 * @param <R> the type of the result of the function
 * @see java.util.function.BiFunction
 * @author Isabel Maskrey
 * @since 1.0.0-SNAPSHOT
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {
    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @param v the third function argument
     * @return the function result
     * @since 1.0.0-SNAPSHOT
     */
    R apply(T t, U u, V v);

    /**
     * Returns a composed function that first applies this function to its input, and then applies the {@code after}
     * function to the result. If evaluation of either function throws an exception, it is thrown from the composed
     * function.
     *
     * @param <X> the type of output of the {@code after} function, and of the composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then applies the {@code after} function
     * @throws IllegalArgumentException if {@code after} is null
     * @see java.util.function.Function#andThen(Function)
     * @since 1.0.0-SNAPSHOT
     */
    default <X> TriFunction<T, U, V, X> andThen(final @NotNull Function<? super R, ? extends X> after) {
        Parameters.requireNonNull("after", after);
        return (T t, U u, V v) -> after.apply(apply(t, u, v));
    }
}
