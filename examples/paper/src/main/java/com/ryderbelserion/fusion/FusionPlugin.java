package com.ryderbelserion.fusion;

import com.ryderbelserion.FusionSettings;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class FusionPlugin extends JavaPlugin {

    private final FusionSettings fusionSettings = FusionSettings.get();

    @Override
    public void onEnable() {
        this.fusionSettings.onEnable(this);
    }

    @Override
    public void onDisable() {
        this.fusionSettings.onDisable();
    }

    public @NotNull FusionSettings getFusionSettings() {
        return this.fusionSettings;
    }
}