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
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
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

        final ComponentLogger logger = getComponentLogger();

        logger.warn("==== PLATFORM INDEPENDENT FILEMANAGER START ====");

        final FileManager fileManager = this.api.getFusion().getFileManager();

        fileManager.addFolder("locale", FileType.YAML, null, false)
                .addFile("config.yml", null, null, ConfigKeys.class, SecondKeys.class).init(false);

        @Nullable final JaluCustomFile customFile = fileManager.getJaluFile("config.yml");

        if (customFile != null) {
            final SettingsManager manager = customFile.getSettingsManager();

            logger.warn("Key: {}", manager.getProperty(ConfigKeys.root_key));
            logger.warn("Second Key: {}", manager.getProperty(SecondKeys.second_key));
        }

        @Nullable final YamlCustomFile english = (YamlCustomFile) fileManager.getCustomFile("locale", "en-US.yml");

        if (english != null) {
            logger.warn("String: {}", english.getStringValue("root", "reload-plugin"));
        }

        @Nullable final YamlCustomFile german = (YamlCustomFile) fileManager.getCustomFile("locale", "en-DE.yml");

        if (german != null) {
            logger.warn("String: {}", german.getStringValue("root", "reload-plugin"));
        }

        fileManager.getCustomFiles().forEach((key, value) -> {
            logger.warn("File: {}", key);

            value.forEach((name, custom) -> logger.warn("Custom Key: {}", name));
        });

        logger.warn("==== PLATFORM INDEPENDENT FILEMANAGER END ====");

        logger.warn("==== PAPER FILEMANAGER START ====");

        final com.ryderbelserion.fusion.paper.files.FileManager paper = this.api.getFileManager();

        paper.addFolder("codes", FileType.YAML).addFolder("vouchers", FileType.YAML);

        logger.warn("==== PAPER FILEMANAGER END ====");

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