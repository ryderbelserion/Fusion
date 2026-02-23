package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

public class Fusion extends JavaPlugin {

    private FusionPaper fusion;

    @Override
    public void onEnable() {
        this.fusion = new FusionPaper(this, getFile().toPath());
        this.fusion.init();

        final Path source = getFile().toPath();
        final Path path = getDataPath();

        final FileManager fileManager = this.fusion.getFileManager();

        //fileManager.extractFolder(source, "velocity", ""); // extract to root dir
        //fileManager.extractFolder(source, "discord"); // extract previously.

        fileManager.addFile( // extract normally
                path.resolve("test.yml"),
                FileType.YAML
        );

        fileManager.addFolder(path.resolve("crates"), FileType.YAML); // extract normally

        fileManager.addFile(path.resolve("config.yml"), FileType.YAML, action -> {
            action.addAction(FileAction.ALREADY_EXTRACTED);
        });

        fileManager.addFolder(path.resolve("discord"), FileType.YAML, action -> {
            action.addAction(FileAction.ALREADY_EXTRACTED);
        });
    }

    public @NotNull final FusionPaper getFusion() {
        return this.fusion;
    }
}