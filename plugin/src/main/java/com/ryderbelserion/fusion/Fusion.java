package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.files.PaperFileManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

public class Fusion extends JavaPlugin {

    private final FusionPaper fusion;

    public Fusion(@NotNull final FusionPaper fusion) {
        this.fusion = fusion;
    }

    @Override
    public void onEnable() {
        this.fusion.setPlugin(this).init();

        final PaperFileManager fileManager = this.fusion.getFileManager();

        final Path path = getDataPath();

        fileManager.extractFile("config.yml", path.resolve("guis").resolve("config.yml"));
    }
}