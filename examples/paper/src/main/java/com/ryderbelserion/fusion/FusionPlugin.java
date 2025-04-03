package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.api.enums.FileType;
import com.ryderbelserion.fusion.api.files.FileManager;
import com.ryderbelserion.fusion.api.files.types.YamlCustomFile;
import com.ryderbelserion.fusion.commands.CommandManager;
import com.ryderbelserion.fusion.config.ConfigKeys;
import com.ryderbelserion.fusion.config.SecondKeys;
import com.ryderbelserion.fusion.core.api.LoggerImpl;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
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

        final LoggerImpl logger = this.api.getLogger();

        final Path path = getDataPath();

        //FileUtils.getFiles(path.resolve("vouchers"), ".yml", 2).forEach(folder -> logger.warn("<red>Folder: <yellow>{}", folder.getFileName()));

        //FileUtils.getFiles(path, ".yml").forEach(folder -> logger.warn("<red>File: <green>{}", folder.getFileName()));

        logger.warn("<red>==== PLATFORM INDEPENDENT FILEMANAGER START ====");

        final FileManager fileManager = this.api.getFileManager();

        final Path locale = path.resolve("locale");

        fileManager.addFolder(locale, FileType.YAML, Optional.empty(), false)
                .addFile(path.resolve("config.yml"), Optional.empty(), Optional.empty(), false, ConfigKeys.class, SecondKeys.class)
                .init(false);

        final YamlCustomFile english = fileManager.getYamlFile(locale.resolve("en-US.yml"));

        if (english != null) {
            logger.warn("<yellow>String: {}", english.getStringValue("root", "reload-plugin"));
        }

        final YamlCustomFile german = fileManager.getYamlFile(locale.resolve("en-DE.yml"));

        if (german != null) {
            logger.warn("<yellow>String: {}", german.getStringValue("root", "reload-plugin"));
        }

        //@Nullable final JaluCustomFile customFile = fileManager.getJaluFile("config.yml");

        //if (customFile != null) {
            //final SettingsManager manager = customFile.getSettingsManager();

            //logger.warn("Key: {}", manager.getProperty(ConfigKeys.root_key));
            //logger.warn("Second Key: {}", manager.getProperty(SecondKeys.second_key));
        //}

        fileManager.getCustomFiles().forEach((key, value) -> {
            logger.warn("<light_purple>File: {}", key);

            //value.forEach((name, custom) -> logger.warn("<aqua>Custom Key: {}", name));
        });

        logger.warn("<red>==== PLATFORM INDEPENDENT FILEMANAGER END ====");

        CommandManager.load();

        /*logger.warn("==== PLATFORM INDEPENDENT FILEMANAGER START ====");

        fileManager.addFolder("locale", FileType.YAML, null, false)
                .addFile("config.yml", null, null, ConfigKeys.class, SecondKeys.class).init(false);

        @Nullable final JaluCustomFile customFile = fileManager.getJaluFile("config.yml");

        if (customFile != null) {
            final SettingsManager manager = customFile.getSettingsManager();

            logger.warn("Key: {}", manager.getProperty(ConfigKeys.root_key));
            logger.warn("Second Key: {}", manager.getProperty(SecondKeys.second_key));
        }*/
    }

    @Override
    public void onDisable() {
        this.api.save();
    }

    public FusionPaper getApi() {
        return this.api;
    }
}