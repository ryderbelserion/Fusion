package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.commands.brigadier.FeatureManager;
import com.ryderbelserion.paper.FusionApi;
import org.bukkit.plugin.java.JavaPlugin;

public class FusionPlugin extends JavaPlugin {

    public static FusionPlugin getPlugin() {
        return JavaPlugin.getPlugin(FusionPlugin.class);
    }

    private final FusionApi api = FusionApi.get();

    @Override
    public void onEnable() {
        this.api.enable(this);

        new FeatureManager();
    }

    @Override
    public void onDisable() {
        this.api.disable();
    }

    public FusionApi getApi() {
        return this.api;
    }
}