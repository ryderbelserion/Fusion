package com.ryderbelserion.fusion;

import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.commands.BaseCommand;
import com.ryderbelserion.fusion.config.Config;
import com.ryderbelserion.fusion.core.files.FileAction;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.FileType;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.api.commands.PaperCommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Fusion extends JavaPlugin {

    private FileManager fileManager;
    private FusionPaper paper;

    @Override
    public void onEnable() {
        this.paper = new FusionPaper(this);
        this.paper.enable(this);

        this.fileManager = this.paper.getFileManager();

        final Path path = getDataPath();

        /*FileUtils.extract("ores.json", path.resolve("cache"), new ArrayList<>() {{
            add(FileAction.EXTRACT_FILE);
        }});

        FileUtils.extract("actions.json", path, new ArrayList<>());*/

        this.fileManager.addFile(path.resolve("config.yml"),
                builder -> builder.useDefaultMigrationService().configurationData(Config.class),
                new ArrayList<>(),
                YamlFileResourceOptions.builder().charset(Charset.defaultCharset()).indentationSize(2).build());

        this.fileManager.addFile(path.resolve("actions.yml"), new ArrayList<>(), null);
        this.fileManager.addFile(path.resolve("actions.json"), new ArrayList<>(), null);

        this.fileManager.addFile(path.resolve("cache").resolve("ores.json"), new ArrayList<>() {{
            add(FileAction.EXTRACT_FILE);
        }}, null);

        this.paper.getLegacyFileManager().addFolder("crates", FileType.YAML);

        //this.fileManager.addFolder(path.resolve("crates"), FileType.YAML, new ArrayList<>(), null);

        final PaperCommandManager commandManager = this.paper.getCommandManager();

        commandManager.enable(new BaseCommand(), "The base command for Fusion!", List.of());
    }

    @Override
    public void onDisable() {
        this.paper.disable();
    }

    public final FileManager getFileManager() {
        return this.fileManager;
    }

    public final FusionPaper getPaper() {
        return this.paper;
    }
}