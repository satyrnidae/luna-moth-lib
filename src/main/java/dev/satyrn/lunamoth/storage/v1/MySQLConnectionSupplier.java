package dev.satyrn.lunamoth.storage.v1;

import dev.satyrn.lunamoth.util.v1.Cast;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.net.PasswordAuthentication;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Configures and provides connections to a MySQL database.
 *
 * @author Isabel Maskrey
 * @since 1.0-SNAPSHOT
 */
public final class MySQLConnectionSupplier implements ConnectionSupplier, AutoCloseable {
    private static final @NotNull Logger MYSQL_CONNECTION_LOGGER = Logger.getLogger(MySQLConnectionSupplier.class.getName());
    private final @NotNull Map<String, String> flags = HashMap.newHashMap(8);
    private int port = 3306;
    private @NotNull String hostname = "localhost";
    private final @NotNull String database;
    private final @NotNull PasswordAuthentication credentials;
    private boolean isClosed = false;

    /**
     * Creates a new {@code MySQLConnectionSupplier} with the default {@code hostname} "localhost" and the default
     * {@code port} 3306 for the specified {@code database}.
     *
     * @param database The database name.
     * @param credentials The credentials to use when logging in to the database.
     * @throws IllegalArgumentException if {@code database} is {@code null} or {@code credentials} is {@code null}.
     * @since 1.0-SNAPSHOT
     */
    public MySQLConnectionSupplier(final @NotNull String database,
                                   final @NotNull PasswordAuthentication credentials) {
        this.database = database;
        this.credentials = credentials;
    }

    /**
     * Creates a new {@code MySQLConnectionSupplier} with the default {@code port} 3306 for the specified
     * {@code database} on the server identified by the specified {@code hostname}.
     * @param hostname The host name for the database server.
     * @param database The database name.
     * @param credentials The credentials to use when logging in to the database.
     * @throws IllegalArgumentException if {@code hostname}, {@code database}, or {@code credentials} is {@code null}.
     * @since 1.0-SNAPSHOT
     */
    public MySQLConnectionSupplier(final @NotNull String hostname,
                                   final @NotNull String database,
                                   final @NotNull PasswordAuthentication credentials) {
        this(database, credentials);
        this.hostname = hostname;
    }

    /**
     * Creates a new {@code MySQLConnectionSupplier}.
     * @param hostname The host name for the database server.
     * @param port The port that the database server is listening on.
     * @param database The database name.
     * @param credentials The credentials to use when logging in to the database.
     * @throws IllegalArgumentException if {@code hostname}, {@code database}, or {@code credentials} is {@code null},
     *                                  or if {@code port} is below 0 or above 65535.
     * @since 1.0-SNAPSHOT
     */
    public MySQLConnectionSupplier(final @NotNull String hostname,
                                   final int port,
                                   final @NotNull String database,
                                   final @NotNull PasswordAuthentication credentials) {
        this(hostname, database, credentials);

        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("port must be within the range [0, 65535]!");
        }

        this.port = port;
    }

    /**
     * Gets the username from the credentials
     * @return The username
     */
    public @Nullable String getUserName() {
        return this.credentials.getUserName();
    }

    /**
     * Returns a reference to the password from the credentials. This is cleared when this supplier is closed.
     * @return The password.
     */
    public char[] getPassword() {
        return this.credentials.getPassword();
    }

    /**
     * Gets the current flag map.
     * @return A map of flags to apply to the connection.
     * @since 1.0-SNAPSHOT
     */
    @Contract("-> !null")
    public @NotNull @Unmodifiable Map<String, String> getFlags() {
        return Map.copyOf(this.flags);
    }

    /**
     * Sets a single flag on the connection supplier.
     * @param flag The flag to set.
     * @param value The value to set
     * @return The mutated {@code MySQLConnectionSupplier}.
     * @throws IllegalArgumentException if {@code flag} is {@code null}.
     * @apiNote If {@code value} is set to {@code null} or a blank string and {@code flag} is set, the mapped value of
     *          {@code flag} will be cleared.
     * @since 1.0-SNAPSHOT
     */
    @SuppressWarnings("UnusedReturnValue")
    @Contract(value = "_, _ -> this", mutates = "this")
    public @NotNull MySQLConnectionSupplier setFlag(final @NotNull String flag,
                                                    final @Nullable String value) {
        if (value == null || value.isBlank()) {
            this.flags.remove(flag);
        } else {
            this.flags.put(flag, value);
        }
        return this;
    }

    /**
     * Sets multiple flags from a map object.
     * @param flags A map containing all values to map.
     * @return The mutated {@code MySQLConnectionSupplier}.
     * @throws IllegalArgumentException if {@code flags} is {@code null}.
     * @throws UnsupportedOperationException if any key in {@code flags} is {@code null}
     * @apiNote If any value in {@code flags} is set to {@code null}, any matching mapped value in the connection
     *          supplier will be cleared. Also note that all objects in {@code flags} are converted to {@code String}
     *          keys and values, so every object in {@code flags} should be sufficiently represented by the value
     *          returned by its {@code toString()} method.
     * @since 1.0-SNAPSHOT
     */
    @SuppressWarnings("UnusedReturnValue")
    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull MySQLConnectionSupplier setFlags(final @NotNull @Unmodifiable Map<?, ?> flags) {
        if (flags.keySet().stream().anyMatch(Objects::isNull)) {
            throw new UnsupportedOperationException("flags cannot contain null keys!");
        }
        for (final @NotNull Object key : flags.keySet()) {
            this.setFlag(key.toString(), Cast.toString(flags.get(key)));
        }
        return this;
    }

    /**
     * Sets multiple flags from a list of map objects.
     * @param flagsList A list containing all values to map.
     * @return The mutated {@code MySQLConnectionSupplier}.
     * @throws IllegalArgumentException if {@code flagsList} is {@code null}.
     * @throws UnsupportedOperationException if any key in any map in {@code flagsList} is {@code null}
     * @apiNote Given a single entry in {@code flagsList} called {@code flags}, if any value in {@code flags} is set to
     *          {@code null}, any matching mapped value in the connection supplier will be cleared. Also note that all
     *          objects in {@code flags} are converted to {@code String} keys and values, so every object in
     *          {@code flags} should be sufficiently represented by the value returned by its {@code toString()} method.
     *          Maps in {@code flagsList} are parsed beginning to end, so the last map in the list with a certain value
     *          will override the value in all previous maps. {@code null} entries within the {@code flagsList} are
     *          excluded.
     * @since 1.0-SNAPSHOT
     */
    @SuppressWarnings("UnusedReturnValue")
    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull MySQLConnectionSupplier setFlags(final @NotNull @Unmodifiable List<Map<?, ?>> flagsList) {
        flagsList.stream().filter(Objects::nonNull).forEach(this::setFlags);
        return this;
    }

    /**
     * Gets a connection per the current configuration of the supplier instance.
     * @return The connection to the database if successful. Otherwise, returns {@code null}.
     * @throws IllegalStateException if the supplier has been closed.
     * @implNote Once the supplier is done being used, please call {@link #close()} to zero out the password.
     * @since 1.0-SNAPSHOT
     */
    @Contract(value = "-> _", pure = true)
    @Override public @Nullable Connection get() {
        if (this.isClosed) throw new IllegalStateException("supplier has been disposed.");

        final @NotNull StringBuilder connectionURLBuilder = new StringBuilder("jdbc:mysql//")
                .append(this.hostname)
                .append(':')
                .append(this.port)
                .append('/')
                .append(this.database);

        if (!this.flags.isEmpty()) {
            int parameterIndex = 0;
            for(final @NotNull String key : this.flags.keySet()) {
                connectionURLBuilder.append(0 == parameterIndex++ ? '?' : '&')
                        .append(key)
                        .append('=')
                        .append(this.flags.get(key));
            }
        }

        final @NotNull String connectionURL = connectionURLBuilder.toString();
        final @Nullable String userName = this.credentials.getUserName();
        final char[] password = this.credentials.getPassword();
        MYSQL_CONNECTION_LOGGER.log(Level.FINE, "Attempting connection to MySQL-like database at {0} with {1}", new Object[]{
                connectionURL,
                userName == null ? "anonymous user" : "user " + userName
        });
        try {
            return DriverManager.getConnection(connectionURL, userName, String.copyValueOf(password));
        } catch (SQLException ex) {
            MYSQL_CONNECTION_LOGGER.log(Level.SEVERE,
                    String.format("Failed to connect to the MySQL-like database at %s with %s!", connectionURL, userName == null ? "anonymous user" : "user " + userName),
                    ex);
        }
        return null;
    }

    /**
     * Zeroes out the credential password.
     *
     * @since 1.0-SNAPSHOT
     */
    @Override
    public void close() {
        if (!this.isClosed) {
            Arrays.fill(this.credentials.getPassword(), '\0');
            this.isClosed = true;
        }
    }
}
