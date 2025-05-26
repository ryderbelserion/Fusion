package com.ryderbelserion.fusion;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.commands.CommandHandler;
import com.ryderbelserion.fusion.config.Config;
import com.ryderbelserion.fusion.core.api.events.EventBuilder;
import com.ryderbelserion.fusion.core.files.FileAction;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.types.JaluCustomFile;
import com.ryderbelserion.fusion.core.files.types.JsonCustomFile;
import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import com.ryderbelserion.fusion.events.CreatureSpawnEvent;
import com.ryderbelserion.fusion.paper.FusionPaper;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;

public class Fusion extends JavaPlugin {

    private FusionPaper paper;
    private FileManager fileManager;

    @Override
    public void onEnable() {
        this.paper = new FusionPaper(this);

        this.fileManager = this.paper.getFileManager();

        final Path path = getDataPath();

        this.fileManager.addFile(path.resolve("config.yml"), builder -> builder.useDefaultMigrationService().configurationData(Config.class), new ArrayList<>(), YamlFileResourceOptions.builder()
                .charset(Charset.defaultCharset()).indentationSize(2).build());

        this.fileManager.addFile(path.resolve("actions.yml"), new ArrayList<>() {{
            add(FileAction.EXTRACT);
        }}, configurationOptions -> configurationOptions.header("This is a header for a yaml file!"));
        this.fileManager.addFile(path.resolve("actions.json"), new ArrayList<>() {{
            add(FileAction.EXTRACT);
        }}, configurationOptions -> configurationOptions.header("This is a header for a json file!"));

        final ComponentLogger logger = getComponentLogger();

        final JsonCustomFile json = this.fileManager.getJsonFile(path.resolve("actions.json"));

        if (json != null) {
            logger.warn("Json File: {}", json.getConfiguration().node("value").getBoolean(true));
        }

        final YamlCustomFile yaml = this.fileManager.getYamlFile(path.resolve("actions.yml"));

        if (yaml != null) {
            logger.warn("Yaml File: {}", yaml.getConfiguration().node("value").getBoolean(false));
        }

        final JaluCustomFile jalu = this.fileManager.getJaluFile(path.resolve("config.yml"));

        if (jalu != null) {
            final SettingsManager config = jalu.getConfiguration();

            logger.warn("Prefix: {}", config.getProperty(Config.prefix));
        }

        final EventBuilder builder = this.paper.getEventBuilder();

        builder.addModule(new CreatureSpawnEvent()).load();

        new CommandHandler();
    }

    public final FileManager getFileManager() {
        return this.fileManager;
    }

    public final FusionPaper getPaper() {
        return this.paper;
    }
}