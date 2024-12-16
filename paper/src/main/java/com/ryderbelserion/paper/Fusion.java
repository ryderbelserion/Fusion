package com.ryderbelserion.paper;

import com.ryderbelserion.FusionLayout;
import com.ryderbelserion.FusionSettings;
import org.bukkit.plugin.Plugin;
import java.io.File;

public class Fusion implements FusionLayout {

    private final FusionSettings settings = FusionSettings.get();

    private final Plugin plugin = this.settings.getPlugin();

    @Override
    public void enable() {

    }

    @Override
    public File getDataFolder() {
        return this.plugin.getDataFolder();
    }
}