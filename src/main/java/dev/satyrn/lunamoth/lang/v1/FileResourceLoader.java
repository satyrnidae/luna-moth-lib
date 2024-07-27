package dev.satyrn.lunamoth.lang.v1;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

/**
 * Custom resource loader which can load files from the disk.
 * <p>
 * Cannot be used to find, define, or load classes.
 *
 * @author Isabel Maskrey
 * @since 1.0-SNAPSHOT
 */
public class FileResourceLoader extends ClassLoader {
    private final @NotNull String baseDirectory;

    /**
     * Creates a new instance of {@code FileResourceLoader}.
     *
     * @param  loader        The parent class loader
     * @param  baseDirectory The base directory to search
     * @throws NullPointerException if {@code baseDirectory} is null
     * @since  1.0-SNAPSHOT
     */
    public FileResourceLoader(final @Nullable ClassLoader loader,
                              final @NotNull String baseDirectory) {
        super(loader);

        Objects.requireNonNull(baseDirectory);
        this.baseDirectory = baseDirectory;
    }

    /**
     * Attempts to load a class with this classloader.
     * <p>
     * Always throws {@code UnsupportedOperationException}, as this loader does not load classes for security reasons.
     *
     * @param  name the <a href="#binary-name">binary name</a> of the class
     * @return the loaded class
     * @throws NullPointerException if {@code name} is {@code null}
     * @throws UnsupportedOperationException always, as this loader does not support loading classes
     * @since  1.0-SNAPSHOT
     */
    @Contract("_ -> fail")
    @Override
    public @NotNull Class<?> loadClass(final @NotNull String name) throws UnsupportedOperationException {
        Objects.requireNonNull(name);
        throw new UnsupportedOperationException("loading classes from file directories is not allowed.");
    }

    /**
     * Finds a resource with the given {@code name} in the {@code baseDirectory}.
     *
     * @param  name the resource name
     * @return a {@code URL} object representing the resouce's location, or {@code null} if the resource could not
     *         be located.
     * @throws NullPointerException if {@code name} is {@code null}
     * @since  1.0-SNAPSHOT
     */
    @Override
    public @Nullable URL getResource(final @NotNull String name) {
        Objects.requireNonNull(name);
        final @NotNull File file = new File(this.baseDirectory, name);
        if (file.exists()) {
            try {
                return file.toURI().toURL();
            } catch (final MalformedURLException ignored) { }
        }

        return null;
    }

    /**
     * Finds a resource with the given {@code name} in the {@code baseDirectory} of this file loader, and creates a new
     * {@code InputStream} to read the resource's contents.
     *
     * @param  name the resource name
     * @return An input stream for reading resource data, or {@code null} if the resource is inaccessible. This could be
     *         because the resource does not exist, or if access to the resource was denied.
     * @throws NullPointerException if {@code name} is {@code null}
     * @since  1.0-SNAPSHOT
     */
    @Override
    public @Nullable InputStream getResourceAsStream(final @NotNull String name) {
        Objects.requireNonNull(name);
        final @NotNull File file = new File(this.baseDirectory, name);
        if (file.exists()) {
            try {
                return new FileInputStream(file);
            } catch (final FileNotFoundException ignored) { }
        }

        return null;
    }
}
