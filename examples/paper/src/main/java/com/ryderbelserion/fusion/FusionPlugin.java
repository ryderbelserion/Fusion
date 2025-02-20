package com.ryderbelserion.fusion;

import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.fusion.config.ConfigKeys;
import com.ryderbelserion.fusion.config.SecondKeys;
import com.ryderbelserion.fusion.core.api.enums.FileType;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.types.JaluCustomFile;
import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import com.ryderbelserion.fusion.paper.FusionApi;
import com.ryderbelserion.fusion.commands.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public class FusionPlugin extends JavaPlugin {

    public static FusionPlugin getPlugin() {
        return JavaPlugin.getPlugin(FusionPlugin.class);
    }

    private final FusionApi api = FusionApi.get();

    @Override
    public void onEnable() {
        this.api.enable(this);

        final FileManager fileManager = this.api.getFusion().getFileManager();

        fileManager.addFolder("locale", FileType.YAML, null)
                .addFile("config.yml", null, null, ConfigKeys.class, SecondKeys.class).init();

        @Nullable final JaluCustomFile customFile = fileManager.getJaluFile("config.yml");

        if (customFile != null) {
            final SettingsManager manager = customFile.getSettingsManager();

            getComponentLogger().warn("Key: {}", manager.getProperty(ConfigKeys.root_key));
            getComponentLogger().warn("Second Key: {}", manager.getProperty(SecondKeys.second_key));
        }

        @Nullable final YamlCustomFile english = (YamlCustomFile) fileManager.getFile("en-US.yml");

        if (english != null) {
            getComponentLogger().warn("String: {}", english.getStringValue("root", "reload-plugin"));
        }

        @Nullable final YamlCustomFile german = (YamlCustomFile) fileManager.getFile("en-DE.yml");

        if (german != null) {
            getComponentLogger().warn("String: {}", german.getStringValue("root", "reload-plugin"));
        }

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