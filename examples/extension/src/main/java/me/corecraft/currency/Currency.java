package me.corecraft.currency;

import com.ryderbelserion.fusion.addons.api.Extension;
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

        getLogger().warn("The extension is enabling!");
    }

    @Override
    public void onDisable() {
        getLogger().warn("The extension is disabled!");
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