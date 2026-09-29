package com.badbones69.weebs;

import com.ryderbelserion.fusion.addons.api.Extension;
import java.io.IOException;
import java.nio.file.Files;

public class WeebsAddon extends Extension {

    @Override
    public void onEnable() {
        try {
            Files.createFile(getDataDirectory().resolve("config.yml"));
        } catch (final IOException exception) {
            exception.printStackTrace();
        }

        getLogger().info("Guten Tag!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Guten Nacht!");
    }
}