package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.plugin.java.JavaPlugin;
import java.nio.file.Path;

public class Fusion extends JavaPlugin {

    @Override
    public void onEnable() {
        final FusionPaper fusion = new FusionPaper(this);

        final FileManager fileManager = fusion.getFileManager();

        final Path path = getDataPath();

        fileManager.extractFile("config.yml", path.resolve("guis").resolve("config.yml"));
    }
}