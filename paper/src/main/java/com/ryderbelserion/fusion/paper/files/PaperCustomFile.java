package com.ryderbelserion.fusion.paper.files;

import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.interfaces.ICustomFile;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;

public class PaperCustomFile extends ICustomFile<PaperCustomFile, YamlConfiguration, Object, Object> {

    public PaperCustomFile(@NotNull final FileManager fileManager) {
        super(fileManager);
    }

    @Override
    public @NotNull YamlConfiguration loadConfig() {
        return YamlConfiguration.loadConfiguration(getPath().toFile());
    }

    @Override
    public void saveConfig() throws IOException {
        this.configuration.save(getPath().toFile());
    }
}