package com.ryderbelserion;

import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.enums.FileAction;
import com.ryderbelserion.fusion.core.files.types.JaluCustomFile;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.files.PaperFileManager;
import com.ryderbelserion.fusion.paper.files.types.PaperCustomFile;
import com.ryderbelserion.keys.ConfigKeys;
import com.ryderbelserion.listeners.ItemListener;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.nio.charset.Charset;
import java.util.Optional;

public class CrazyCrates extends JavaPlugin {

    private FusionPaper fusion;

    @Override
    public void onEnable() {
        this.fusion = new FusionPaper(this);

        final PaperFileManager fileManager = this.fusion.getFileManager();

        fileManager.addPaperFile(getDataPath().resolve("test.yml"), consumer -> consumer.addAction(FileAction.EXTRACT_FILE))
                .addFile(getDataPath().resolve("config.yml"),
                options -> options.indentationSize(2).charset(Charset.defaultCharset()),
                builder -> builder.configurationData(ConfigKeys.class));

        final ComponentLogger logger = getComponentLogger();

        final Optional<JaluCustomFile> customFile = fileManager.getJaluFile(getDataPath().resolve("config.yml"));

        if (customFile.isPresent()) {
            final SettingsManager config = customFile.get().getConfiguration();

            logger.warn("Test: {}", config.getProperty(ConfigKeys.test));
        }

        final Optional<PaperCustomFile> paperCustomFile = fileManager.getPaperFile(getDataPath().resolve("test.yml"));

        if (paperCustomFile.isPresent()) {
            final PaperCustomFile file = paperCustomFile.get();

            final YamlConfiguration configuration = file.getConfiguration();

            logger.warn("Configuration: {}", configuration.getString("test-value", "N/A"));
        }

        getServer().getPluginManager().registerEvents(new ItemListener(), this);
    }

    public @NotNull final FusionPaper getFusion() {
        return this.fusion;
    }
}