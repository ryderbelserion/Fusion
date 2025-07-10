package com.ryderbelserion.fusion.paper.files.types;

import com.ryderbelserion.fusion.core.api.enums.FileAction;
import com.ryderbelserion.fusion.core.api.interfaces.files.IConfigFile;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PaperCustomFile extends IConfigFile<PaperCustomFile, YamlConfiguration, YamlConfiguration> {

    public PaperCustomFile(@NotNull final Path path, final List<FileAction> actions) {
        super(path, actions, CompletableFuture.supplyAsync(() -> YamlConfiguration.loadConfiguration(path.toFile())).join());
    }

    @Override
    public @NotNull final YamlConfiguration loadConfig() {
        return YamlConfiguration.loadConfiguration(getPath().toFile());
    }

    @Override
    public void saveConfig() throws IOException {
        this.configuration.save(getPath().toFile());
    }
}