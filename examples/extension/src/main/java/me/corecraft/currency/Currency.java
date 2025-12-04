package me.corecraft.currency;

import com.ryderbelserion.fusion.addons.v2.api.Extension;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Currency extends Extension {

    @Override
    public void onEnable() {
        final Path path = getDataDirectory();

        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (final IOException exception) {
                throw new IllegalStateException("Cannot enable the extension, the folder %s did not get created.".formatted(path));
            }
        }
    }

    @Override
    public void onDisable() {

    }

    @Override
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
}