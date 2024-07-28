package dev.satyrn.lunamoth.storage.v1;

import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.util.function.Supplier;

/**
 * Creates connections to a data source.
 *
 * @author Isabel Maskrey
 * @since 1.0-SNAPSHOT
 */
public interface ConnectionSupplier extends Supplier<Connection> {
    /**
     * Opens a connection to the data source.
     *
     * @return The connection to the data source.
     * @since 1.0-SNAPSHOT
     */
    @Nullable Connection get();
}
