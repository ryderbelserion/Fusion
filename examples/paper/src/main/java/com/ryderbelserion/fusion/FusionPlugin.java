package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.core.utils.FileUtils;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.plugin.java.JavaPlugin;

public class FusionPlugin extends JavaPlugin {

    public static FusionPlugin getPlugin() {
        return JavaPlugin.getPlugin(FusionPlugin.class);
    }

    private FusionPaper api = null;

    @Override
    public void onEnable() {
        this.api = new FusionPaper(getComponentLogger(), getDataPath());
        this.api.enable(this);

        FileUtils.extract("guis", getDataPath().resolve("examples"), true);
    }

    public FusionPaper getApi() {
        return this.api;
    }
}