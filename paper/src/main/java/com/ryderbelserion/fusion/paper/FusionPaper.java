package com.ryderbelserion.fusion.paper;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.paper.files.PaperFileManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FusionPaper extends FusionCore {

    private final PaperFileManager fileManager;

    public FusionPaper(@NotNull final JavaPlugin plugin) {
        super(consumer -> {
            consumer.setDataPath(plugin.getDataPath());
        });

        this.fileManager = new PaperFileManager(this);

        init();
    }

    @Override
    public FusionPaper init() {
        final Path dataPath = getDataPath();

        if (Files.notExists(dataPath)) {
            try {
                Files.createDirectory(dataPath);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        return this;
    }

    @Override
    public PaperFileManager getFileManager() {
        return this.fileManager;
    }
}