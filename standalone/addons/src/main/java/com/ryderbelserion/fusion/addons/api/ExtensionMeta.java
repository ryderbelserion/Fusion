package com.ryderbelserion.fusion.addons.api;

import com.ryderbelserion.fusion.addons.api.interfaces.IExtensionMeta;
import org.jspecify.annotations.NonNull;
import org.tinylog.Logger;
import org.tinylog.TaggedLogger;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public abstract class ExtensionMeta implements IExtensionMeta {

    private TaggedLogger logger;
    private String version;
    private String main;
    private String name;

    private Path path;

    public void init(@NonNull final Path parent, @NonNull final Path path) {
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

        this.logger = Logger.tag(this.name);

        this.logger.warn("Loading the extension {}.", this.name);

        this.path = parent.resolve(this.name);

        if (!Files.exists(this.path)) {
            try {
                Files.createDirectory(this.path);
            } catch (final IOException ignored) {
                this.logger.warn("Failed to create directory {}", this.path);
            }
        }
    }

    @Override
    public void info(final String message) {
        this.logger.info(message);
    }

    @Override
    public void warn(final String message) {
        this.logger.warn(message);
    }

    @Override
    public void error(final String message) {
        this.logger.error(message);
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
    public TaggedLogger getLogger() {
        return this.logger;
    }

    @Override
    public String getName() {
        return this.name;
    }
}