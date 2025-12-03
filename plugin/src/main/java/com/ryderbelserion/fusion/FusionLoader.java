package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.paper.FusionPaper;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class FusionLoader implements PluginBootstrap {

    private FusionPaper fusion;

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        this.fusion = new FusionPaper(context);

        /*this.fusion.log("info", "We are starting the server!");

        this.fusion.log("info", StringUtils.toString(List.of(
                "beans",
                "alpha"
        )));*/
    }

    @Override
    public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
        return new Fusion(this.fusion);
    }
}