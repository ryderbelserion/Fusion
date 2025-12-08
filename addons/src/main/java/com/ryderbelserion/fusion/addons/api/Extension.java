package com.ryderbelserion.fusion.addons.api;

import com.ryderbelserion.fusion.addons.api.interfaces.IExtension;
import com.ryderbelserion.fusion.addons.entrypoint.classloaders.SimpleExtensionClassLoader;
import com.ryderbelserion.fusion.addons.exceptions.InvalidExtensionException;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Path;

public class Extension extends IExtension {

    private SimpleExtensionClassLoader classLoader;

    public Extension() {}

    @Override
    public void init(@NotNull final Path parent, @NotNull final Path path) {
        super.init(parent, path);

        try {
            this.classLoader = new SimpleExtensionClassLoader(
                    path,
                    parent,
                    this,
                    getClass().getClassLoader()
            );
        } catch (final IOException | InvalidExtensionException exception) {
            throw new RuntimeException(exception);
        }

        setEnabled(true);
    }

    private boolean isEnabled = false;

    @Override
    public void setEnabled(final boolean isEnabled) {
        if (this.isEnabled != isEnabled) {
            this.isEnabled = isEnabled;

            if (this.isEnabled) {
                onEnable();
            } else {
                onDisable();
            }
        }
    }

    @Override
    public final boolean isEnabled() {
        return this.isEnabled;
    }
}