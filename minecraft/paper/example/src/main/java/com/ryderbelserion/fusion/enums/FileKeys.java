package com.ryderbelserion.fusion.enums;

import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.files.FileException;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.types.configurate.JsonCustomFile;
import com.ryderbelserion.fusion.files.types.configurate.YamlCustomFile;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.nio.file.Path;
import java.util.Optional;

public enum FileKeys {

    messages("messages.yml");

    private final FusionPaper fusion = (FusionPaper) FusionProvider.getInstance();

    private final FileManager fileManager = this.fusion.getFileManager();

    private final Path path = this.fusion.getDataPath();

    private final Path destination; // the file location
    private final Path folder;

    FileKeys(@NotNull final String fileName, @NotNull final String folder) {
        this.folder = this.path.resolve(folder);
        this.destination = this.folder.resolve(fileName);
    }

    FileKeys(@NotNull final String name) {
        this.destination = this.path.resolve(name);
        this.folder = this.path;
    }

    public @NotNull final BasicConfigurationNode getJsonConfig() {
        return getJsonCustomFile().getConfiguration();
    }

    public JsonCustomFile getJsonCustomFile() {
        @NotNull final Optional<JsonCustomFile> customFile = this.fileManager.getJsonFile(this.destination);

        if (customFile.isEmpty()) {
            throw new FileException("Could not find custom file for " + this.destination);
        }

        return customFile.get();
    }

    public @NotNull final CommentedConfigurationNode getYamlConfig() {
        return getYamlCustomFile().getConfiguration();
    }

    public @NotNull final YamlCustomFile getYamlCustomFile() {
        @NotNull final Optional<YamlCustomFile> customFile = this.fileManager.getYamlFile(this.destination);

        if (customFile.isEmpty()) {
            throw new FileException("Could not find custom file for " + this.destination);
        }

        return customFile.get();
    }

    public @NotNull final Path getDestination() {
        return this.destination;
    }

    public void init() {
        this.fileManager.addFile(this.destination, FileType.YAML);
    }
}