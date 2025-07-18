package com.ryderbelserion.fusion.paper.files.types;

import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.interfaces.ICustomFile;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class PaperCustomFile extends ICustomFile<PaperCustomFile, YamlConfiguration, Object, Object> {

    private final File file;

    public PaperCustomFile(@NotNull final FileManager fileManager, @NotNull final Consumer<PaperCustomFile> consumer) {
        super(fileManager);

        this.file = getPath().toFile();

        consumer.accept(this);
    }

    @Override
    public @NotNull YamlConfiguration loadConfig() {
        return YamlConfiguration.loadConfiguration(this.file);
    }

    @Override
    public void saveConfig() throws IOException {
        this.configuration.save(this.file);
    }
}