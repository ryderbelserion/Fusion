package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.api.utils.FileUtils;
import com.ryderbelserion.fusion.core.api.LoggerImpl;
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

        final LoggerImpl logger = this.api.getLogger();

        FileUtils.getFiles("vouchers", getDataPath(), ".yml", 2).forEach(folder -> {
            logger.warn("<red>Folder: <yellow>{}", folder.getFileName());
        });

        FileUtils.getFiles(getDataPath(), ".yml").forEach(folder -> {
            logger.warn("<red>File: <green>{}", folder.getFileName());
        });

        /*logger.warn("==== PLATFORM INDEPENDENT FILEMANAGER START ====");

        final FileManager fileManager = this.api.getFileManager();

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

        final LegacyFileManager paper = this.api.getLegacyFileManager();

        paper.addFolder("codes", FileType.YAML).addFolder("vouchers", FileType.YAML).addFile("data.yml");

        final LegacyCustomFile file = paper.getFile("Starter-Money.yml", FileType.YAML);

        if (file != null) {
            final YamlConfiguration configuration = file.getConfiguration();

            if (configuration != null) {
                logger.warn("Code: {}", configuration.getString("voucher-code.code", "beans"));
            }
        } else {
            logger.warn("The file is null.");
        }

        logger.warn("==== PAPER FILEMANAGER END ====");

        CommandManager.load();*/
    }

    @Override
    public void onDisable() {
        this.api.save();
    }

    public FusionPaper getApi() {
        return this.api;
    }
}