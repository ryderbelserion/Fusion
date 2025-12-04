package com.ryderbelserion.fusion.addons.v2;

import com.ryderbelserion.fusion.addons.utils.FileUtils;
import com.ryderbelserion.fusion.addons.v2.api.Extension;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionManager {

    private final Logger logger = LoggerFactory.getLogger(ExtensionManager.class);

    private final Map<String, Extension> extensions = new ConcurrentHashMap<>();
    private final Map<String, Class<?>> classMap = new ConcurrentHashMap<>();

    private final Path parent; // parent path i.e. the extensions folder

    public ExtensionManager(@NotNull final Path parent) {
        this.parent = parent;
    }

    public void init(final int depth) {
        try {
            Files.createDirectories(this.parent);
        } catch (final IOException exception) {
            throw new IllegalStateException("Could not create folder %s!".formatted(this.parent), exception);
        }

        final List<Path> paths = FileUtils.getFiles(this.parent, List.of(".jar"), depth);

        this.logger.warn("Initializing extensions...");

        for (final Path path : paths) {
            loadExtension(path);
        }

        this.logger.warn("Initialized {} extension(s)", this.extensions.size());
    }

    public void loadExtension(@NotNull final Path path) {
        final Extension extension = new Extension();

        extension.init(this, this.parent, path);

        final String name = extension.getName();

        if (this.extensions.containsKey(name)) {
            throw new IllegalStateException("Cannot have 2 extensions with the same name! Extension Name: %s".formatted(name));
        }

        this.extensions.put(name, extension);

        extension.onEnable();
    }

    public final boolean isExtensionEnabled(@NotNull final String name) {
        final Optional<Extension> extension = getExtension(name);

        return extension.map(Extension::isEnabled).orElse(false);
    }

    public Optional<Extension> getExtension(@NotNull final String name) {
        return Optional.ofNullable(this.extensions.get(name));
    }
}