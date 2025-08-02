package com.ryderbelserion;

import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.keys.ConfigKeys;
import com.ryderbelserion.listeners.ItemListener;
import net.kyori.adventure.key.Key;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CrazyCrates extends JavaPlugin {

    private FusionPaper fusion;

    @Override
    public void onEnable() {
        this.fusion = new FusionPaper(this);

        final FileManager fileManager = this.fusion.getFileManager();

        fileManager.addFile(Key.key("config.yml"), builder -> builder.configurationData(ConfigKeys.class));

        getServer().getPluginManager().registerEvents(new ItemListener(), this);
    }

    public @NotNull final FusionPaper getFusion() {
        return this.fusion;
    }
}