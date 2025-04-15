package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.plugin.java.JavaPlugin;

public class FusionPlugin extends JavaPlugin {

    public static FusionPlugin getPlugin() {
        return JavaPlugin.getPlugin(FusionPlugin.class);
    }

    private final FusionPaper api = null;

    @Override
    public void onEnable() {
        /*this.api = new FusionPaper(getComponentLogger(), getDataPath());
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

        logger.warn("<red>==== LEGACY FILEMANAGER START ====");

        try {
            FileUtils.compress(getDataPath().resolve("vouchers"), Optional.of(getDataPath().resolve("backups")), Optional.empty(), true);
        } catch (IOException exception) {
            logger.warn("Failed to compress: ", exception);
        }

        final LegacyFileManager legacyFileManager = this.api.getLegacyFileManager();

        legacyFileManager.addFolder("vouchers", FileType.YAML);

        legacyFileManager.getFiles().forEach((name, legacyCustomFile) -> logger.warn("<yellow>File Name: {}", name));

        logger.warn("<red>==== LEGACY FILEMANAGER END ====");

        final List<String> files = FileUtils.getNamesWithoutExtension(Optional.of("vouchers"), getDataPath(), ".yml", 2);

        logger.warn("<green>Size No Extension: {}", files.size());

        files.forEach(file -> logger.warn("Voucher Name No Extension: {}", file));

        final List<String> extensions = FileUtils.getNamesByExtension(Optional.of("vouchers"), getDataPath(), ".yml", 2);

        logger.warn("<green>Size Extensions: {}", extensions.size());

        extensions.forEach(file -> logger.warn("Voucher Name Extension: {}", file));

        CommandManager.load();*/
    }
}