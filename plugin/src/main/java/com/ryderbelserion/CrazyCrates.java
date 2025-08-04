package com.ryderbelserion;

import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.fusion.core.api.support.objects.FusionKey;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.keys.ConfigKeys;
import com.ryderbelserion.listeners.ItemListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CrazyCrates extends JavaPlugin {

    private FusionPaper fusion;

    @Override
    public void onEnable() {
        this.fusion = new FusionPaper(this, "crazycrates");

        final FileManager fileManager = this.fusion.getFileManager();

        fileManager.addFile(FusionKey.key("config.yml"), builder -> builder.configurationData(ConfigKeys.class));

        final SettingsManager config = fileManager.getJaluFile(FusionKey.key("config.yml")).getConfiguration();

        getComponentLogger().warn("{}", config.getProperty(ConfigKeys.test));

        getServer().getPluginManager().registerEvents(new ItemListener(), this);
    }

    public @NotNull final FusionPaper getFusion() {
        return this.fusion;
    }
}