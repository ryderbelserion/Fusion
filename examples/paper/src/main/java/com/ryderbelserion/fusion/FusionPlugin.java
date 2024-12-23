package com.ryderbelserion.fusion;

import com.ryderbelserion.paper.FusionApi;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class FusionPlugin extends JavaPlugin {

    private final FusionApi fusionApi = FusionApi.get();

    @Override
    public void onEnable() {
        this.fusionApi.enable(this);
    }

    @Override
    public void onDisable() {
        this.fusionApi.disable();
    }

    public @NotNull FusionApi getFusionSettings() {
        return this.fusionApi;
    }
}