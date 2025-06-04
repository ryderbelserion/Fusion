package com.ryderbelserion.fusion;

import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.commands.BaseCommand;
import com.ryderbelserion.fusion.config.Config;
import com.ryderbelserion.fusion.core.files.FileAction;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;

public class Fusion extends JavaPlugin {

    private FusionPaper paper;
    private FileManager fileManager;

    @Override
    public void onEnable() {
        this.paper = new FusionPaper(this);
        this.paper.enable(this);

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

        new BaseCommand(PaperCommandManager.builder()
                .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
                .buildOnEnable(this));
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