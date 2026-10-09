package com.ryderbelserion.fusion.addons;

import com.ryderbelserion.fusion.addons.api.meta.ExtensionMeta;
import com.ryderbelserion.fusion.addons.entrypoint.classloaders.SimpleExtensionClassLoader;
import com.ryderbelserion.fusion.addons.exceptions.InvalidExtensionException;
import com.ryderbelserion.fusion.addons.utils.FileUtils;
import com.ryderbelserion.fusion.addons.api.Extension;
import com.ryderbelserion.fusion.addons.api.interfaces.IExtensionManager;
import org.jspecify.annotations.NonNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ExtensionManager implements IExtensionManager {

    private final Map<String, Extension> extensions = new ConcurrentHashMap<>();

    private final Logger logger;

    private final Path parent; // parent path i.e. the extensions folder

    public ExtensionManager(@NonNull final Path parent) {
        this.parent = parent;
        this.logger = Logger.getLogger("ExtensionManager");
    }

    @Override
    public void init(final int depth) {
        try {
            Files.createDirectories(this.parent);
        } catch (final IOException exception) {
            throw new IllegalStateException("Could not create folder %s!".formatted(this.parent), exception);
        }

        final List<Path> paths = FileUtils.getFiles(this.parent, List.of(".jar"), depth);

        info("Initializing extensions...");

        for (final Path path : paths) {
            try {
                loadExtension(path);
            } catch (IOException | InvalidExtensionException exception) {
                throw new IllegalStateException("Could not load extension %s!".formatted(path), exception);
            }
        }

        warn("Initialized %s extension(s)!", this.extensions.size());
    }

    @Override
    public void loadExtension(@NonNull final Path path) throws IOException, InvalidExtensionException {
        try (final SimpleExtensionClassLoader loader = new SimpleExtensionClassLoader(
                path,
                this.parent,
                new ExtensionMeta(),
                getClass().getClassLoader()
        )) {
            final Extension extension = loader.getExtension();

            final String name = extension.getName();

            if (this.extensions.containsKey(name)) {
                throw new IllegalStateException("Cannot have 2 extensions with the same name! Extension Name: %s".formatted(name));
            }

            extension.post();

            this.extensions.put(name, extension);
        }
    }

    @Override
    public void disableExtension(@NonNull final Extension extension) {
        if (!extension.isEnabled()) return;

        extension.onDisable();

        extension.setEnabled(false);

        /*final SimpleExtensionClassLoader loader = extension.getClassLoader(); //todo() clean up class loader

        loader.setDisabling(true);
        loader.removeClasses();*/

        this.extensions.remove(extension.getName());
    }

    @Override
    public void reloadExtension(@NonNull final Extension extension) {
        if (!extension.isEnabled()) return;

        extension.onReload();
    }

    @Override
    public final boolean isExtensionEnabled(@NonNull final String name) {
        final Optional<Extension> extension = getExtension(name);

        return extension.map(Extension::isEnabled).orElse(false);
    }

    @Override
    public Optional<Extension> getExtension(@NonNull final String name) {
        return Optional.ofNullable(this.extensions.get(name));
    }

    @Override
    public void purge() {
        this.extensions.values().forEach(this::disableExtension);
        this.extensions.clear();
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
    public int getLoadedExtensionCount() {
        return this.extensions.values().stream().filter(Extension::isEnabled).toList().size();
    }
}