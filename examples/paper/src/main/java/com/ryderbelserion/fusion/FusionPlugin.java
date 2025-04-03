package com.ryderbelserion.fusion;

import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.fusion.api.enums.FileType;
import com.ryderbelserion.fusion.api.files.FileManager;
import com.ryderbelserion.fusion.api.files.types.JaluCustomFile;
import com.ryderbelserion.fusion.api.files.types.YamlCustomFile;
import com.ryderbelserion.fusion.commands.CommandManager;
import com.ryderbelserion.fusion.config.ConfigKeys;
import com.ryderbelserion.fusion.config.SecondKeys;
import com.ryderbelserion.fusion.core.api.managers.LoggerManager;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.plugin.java.JavaPlugin;
import java.nio.file.Path;
import java.util.Optional;

public class FusionPlugin extends JavaPlugin {

    public static FusionPlugin getPlugin() {
        return JavaPlugin.getPlugin(FusionPlugin.class);
    }

    private FusionPaper api = null;

    @Override
    public void onEnable() {
        this.api = new FusionPaper(getComponentLogger(), getDataPath());
        this.api.enable(this);

        final LoggerManager logger = this.api.getLogger();

        final Path path = getDataPath();

        logger.warn("<red>==== PLATFORM INDEPENDENT FILEMANAGER START ====");

        final FileManager fileManager = this.api.getFileManager();

        final Path locale = path.resolve("locale");

        fileManager.addFolder(locale, FileType.YAML, Optional.empty(), false)
                .addFile(path.resolve("config.yml"), Optional.empty(), Optional.empty(), false, false, ConfigKeys.class, SecondKeys.class)
                .init(false);

        final YamlCustomFile english = fileManager.getYamlFile(locale.resolve("en-US.yml"));

        if (english != null) {
            logger.warn("<yellow>String: {}", english.getStringValue("root", "reload-plugin"));
        }

        final YamlCustomFile german = fileManager.getYamlFile(locale.resolve("en-DE.yml"));

        if (german != null) {
            logger.warn("<yellow>String: {}", german.getStringValue("root", "reload-plugin"));
        }

        final JaluCustomFile customFile = fileManager.getJaluFile(path.resolve("config.yml"));

        if (customFile != null) {
            final SettingsManager manager = customFile.getSettingsManager();

            logger.warn("<yellow>Key: {}", manager.getProperty(ConfigKeys.root_key));
            logger.warn("<yellow>Second Key: {}", manager.getProperty(SecondKeys.second_key));
        }

        logger.warn("<red>==== PLATFORM INDEPENDENT FILEMANAGER END ====");

        CommandManager.load();
    }

    @Override
    public void onDisable() {
        this.api.save();
    }

    public FusionPaper getApi() {
        return this.api;
    }
}