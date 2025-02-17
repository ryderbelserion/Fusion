package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.core.util.FileUtils;
import com.ryderbelserion.fusion.paper.FusionApi;
import com.ryderbelserion.fusion.commands.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public class FusionPlugin extends JavaPlugin {

    public static FusionPlugin getPlugin() {
        return JavaPlugin.getPlugin(FusionPlugin.class);
    }

    private final FusionApi api = FusionApi.get();

    @Override
    public void onEnable() {
        this.api.enable(this);

        FileUtils.extracts("/locale/", new File(getDataFolder(), "locale").toPath(), false);

        CommandManager.load();
    }

    @Override
    public void onDisable() {
        this.api.disable();
    }

    public FusionApi getApi() {
        return this.api;
    }
}