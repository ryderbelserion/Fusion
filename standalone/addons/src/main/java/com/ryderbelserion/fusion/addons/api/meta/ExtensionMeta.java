package com.ryderbelserion.fusion.addons.api.meta;

import com.ryderbelserion.fusion.addons.api.interfaces.IExtensionMeta;
import org.jspecify.annotations.NonNull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.logging.Logger;

public class ExtensionMeta implements IExtensionMeta {

    protected String version;
    protected Logger logger;
    protected String main;
    protected String name;

    protected Path parent;
    protected Path path;

    public void init(@NonNull final Path parent, @NonNull final Path path) {
        this.parent = parent;

        final Properties properties = new Properties();

        try (final FileSystem entry = FileSystems.newFileSystem(path, (ClassLoader) null); final InputStream stream = Files.newInputStream(entry.getPath("addon.properties"))) {
            properties.load(stream);
        } catch (final IOException exception) {
            throw new IllegalStateException("Failed to load addon.properties!", exception);
        }

        final String pathName = path.getFileName().toString();

        this.version = properties.getProperty("version", "N/A");
        this.main = properties.getProperty("main", "N/A");
        this.name = properties.getProperty("name", pathName);

        if (this.main.isEmpty() || this.main.equals("N/A")) {
            throw new IllegalStateException("Extension group cannot be empty for %s.".formatted(pathName));
        }

        if (this.name.isEmpty()) {
            throw new IllegalStateException("Extension name cannot be empty for %s.".formatted(pathName));
        }
    }

    public void copy(@NonNull final ExtensionMeta meta) {
        this.parent = meta.getParentDirectory();
        this.version = meta.getVersion();
        this.main = meta.getMainClass();
        this.name = meta.getName();
    }

    public void post() {
        this.logger = Logger.getLogger(this.name);

        info("Loading the extension %s.", this.name);

        this.path = parent.resolve(this.name);

        if (!Files.exists(this.path)) {
            try {
                Files.createDirectory(this.path);
            } catch (final IOException ignored) {
                warn("Failed to create directory %s.", this.path);
            }
        }
    }

    @Override
    public void info(final String message, final Object... params) {
        this.logger.info(message.formatted(params));
    }

    @Override
    public void warn(final String message, final Object... params) {
        this.logger.warning(message.formatted(params));
    }

    @Override
    public void error(final String message, final Object... params) {
        this.logger.severe(message.formatted(params));
    }

    @Override
    public Path getParentDirectory() {
        return this.parent;
    }

    @Override
    public Path getDataDirectory() {
        return this.path;
    }

    @Override
    public String getMainClass() {
        return this.main;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public String getName() {
        return this.name;
    }
}