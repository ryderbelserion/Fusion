package com.ryderbelserion.fusion.addons.v2.api;

import com.ryderbelserion.fusion.addons.v2.ExtensionManager;
import com.ryderbelserion.fusion.addons.v2.api.interfaces.IExtension;
import com.ryderbelserion.fusion.addons.v2.entrypoint.classloaders.SimpleExtensionClassLoader;
import com.ryderbelserion.fusion.addons.v2.exceptions.InvalidExtensionException;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Path;

public class Extension extends IExtension {

    private SimpleExtensionClassLoader classLoader;

    public Extension() {}

    @Override
    public void init(@NotNull final ExtensionManager manager, @NotNull final Path parent, @NotNull final Path path) {
        super.init(manager, parent, path);

        try (final SimpleExtensionClassLoader classLoader = new SimpleExtensionClassLoader(path, parent, this, getClass().getClassLoader())) {
            this.classLoader = classLoader;
        } catch (final IOException | InvalidExtensionException exception) {
            throw new RuntimeException(exception);
        }

        setEnabled(true);
    }

    public @NotNull final SimpleExtensionClassLoader getClassLoader() {
        return this.classLoader;
    }

    private boolean isEnabled = false;

    @Override
    public void setEnabled(final boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    @Override
    public final boolean isEnabled() {
        return this.isEnabled;
    }
}