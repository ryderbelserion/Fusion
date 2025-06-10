package com.ryderbelserion.fusion.example;

import com.ryderbelserion.fusion.example.commands.BaseCommand;
import com.ryderbelserion.fusion.core.files.FileAction;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.FileType;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.api.commands.PaperCommandManager;
import org.bukkit.plugin.java.JavaPlugin;
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

        this.fileManager.addFile(path.resolve("actions.yml"), new ArrayList<>(), null);
        this.fileManager.addFile(path.resolve("actions.json"), new ArrayList<>(), null);

        this.fileManager.addFile(path.resolve("cache").resolve("ores.json"), new ArrayList<>() {{
            add(FileAction.EXTRACT_FILE);
        }}, null);

        this.paper.getLegacyFileManager().addFolder("crates", FileType.YAML);

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