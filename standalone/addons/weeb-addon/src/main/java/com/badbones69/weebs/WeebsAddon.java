package com.badbones69.weebs;

import com.ryderbelserion.fusion.addons.api.Extension;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WeebsAddon extends Extension {

    @Override
    public void onEnable() {
        final Path path = getDataDirectory().resolve("config.yml");

        if (Files.notExists(path)) {
            try {
                Files.createFile(path);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        getLogger().info("Guten Tag!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Guten Nacht!");
    }
}