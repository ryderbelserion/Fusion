package com.ryderbelserion.fusion.paper;

import com.ryderbelserion.fusion.core.FusionConfig;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.files.enums.FileAction;
import com.ryderbelserion.fusion.core.files.enums.FileType;
import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import com.ryderbelserion.fusion.paper.files.PaperFileManager;
import net.kyori.adventure.key.Key;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class FusionPaper extends FusionCore {

    private final PaperFileManager fileManager;

    public FusionPaper(@NotNull final JavaPlugin plugin) {
        super(consumer -> {
            consumer.setDataPath(plugin.getDataPath());
        });

        this.fileManager = new PaperFileManager(this);

        init(consumer -> {
            this.config = new FusionConfig(this.fileManager.getYamlFile(Key.key("fusion")));
        });
    }

    @Override
    public FusionPaper init(@NotNull final Consumer<FusionCore> fusion) {
        final Path dataPath = getDataPath();

        if (Files.notExists(dataPath)) {
            try {
                Files.createDirectory(dataPath);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        this.fileManager.addFile(Key.key("fusion"), FileType.CONFIGURATE, consumer -> {
            final YamlCustomFile customFile = (YamlCustomFile) consumer;

            customFile.setOptions(options -> options.shouldCopyDefaults(true));
            customFile.setPath(dataPath.resolve("fusion.yml"));
            customFile.addAction(FileAction.EXTRACT_FILE);
        });

        fusion.accept(this);

        return this;
    }

    @Override
    public FusionConfig getConfig() {
        return this.config;
    }

    @Override
    public FusionCore reload() {
        this.config.reload();

        return this;
    }

    @Override
    public PaperFileManager getFileManager() {
        return this.fileManager;
    }
}