package dev.satyrn.lunamoth.util.function.v1;

import java.util.function.Function;

/**
 * A functional interface that represents a supplier of results based on a single parameter.
 * <p>
 * This interface defines a method to supply a result {@code R} based on an input of type {@code T}.
 * It extends {@link Function} and provides a default implementation of the {@link Function#apply(Object)}
 * method that delegates to the {@link #get(Object)} method.
 * </p>
 *
 * @param <T> the type of the input parameter to the supplier
 * @param <R> the type of the result supplied by this supplier
 * @see Function
 * @author Isabel Maskrey
 * @since 1.0.0-SNAPSHOT
 */
@FunctionalInterface
public interface ParametricSupplier<T, R> extends Function<T, R> {
    /**
     * Returns a result based on the given input parameter.
     *
     * @param t the input parameter
     * @return the result based on the input parameter
     * @since 1.0.0-SNAPSHOT
     */
    R get(T t);

    /**
     * Applies this {@link Function} to the given input parameter.
     * <p>
     * This default implementation delegates to the {@link #get(Object)} method.
     * </p>
     *
     * @param t the input parameter
     * @return the result of the {@link #get(Object)} method
     * @since 1.0.0-SNAPSHOT
     */
    @Override
    default R apply(T t) {
        return get(t);
    }
}
