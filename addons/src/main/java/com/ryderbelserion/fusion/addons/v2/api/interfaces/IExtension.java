package com.ryderbelserion.fusion.addons.v2.api.interfaces;

import com.ryderbelserion.fusion.addons.v2.api.ExtensionMeta;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class IExtension extends ExtensionMeta {

    public IExtension() {}

    public void onEnable() {
        final Path path = getDataDirectory();

        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (final IOException exception) {
                throw new IllegalStateException("Cannot enable the extension, the folder %s did not get created.".formatted(path));
            }
        }

        setEnabled(true);
    }

    public void onDisable() {
        setEnabled(false);
    }

    public void onReload() {
        final Path path = getDataDirectory();

        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (final IOException exception) {
                throw new IllegalStateException("Cannot enable the extension, the folder %s did not get created.".formatted(path));
            }
        }
    }

    public abstract void setEnabled(final boolean isEnabled);

    public abstract boolean isEnabled();

}