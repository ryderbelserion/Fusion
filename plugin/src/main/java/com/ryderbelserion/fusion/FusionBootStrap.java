package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.paper.FusionPaper;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class FusionBootStrap implements PluginBootstrap {

    private FusionPaper fusion;

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        this.fusion = new FusionPaper(context);
    }

    @Override
    public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
        return new Fusion(this.fusion);
    }
}