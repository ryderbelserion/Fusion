package com.ryderbelserion.fusion.files.interfaces;

import com.ryderbelserion.fusion.files.FileException;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

// I i.e. YamlCustomFile.
// C i.e. the SettingsManager, or CommentedConfigurationNode/BasicConfigurationNode.
// L i.e. the SettingsManagerBuilder, or YamlConfigurationLoader.
// O i.e. options that configure the functionality of L.
public abstract class ICustomFile<I, C, L, O> {

    protected final List<FileAction> actions = new ArrayList<>();

    protected O options = null;
    protected C configuration;
    protected L loader;

    protected FileManager fileManager;
    protected FileType fileType;
    protected Path path;

    public ICustomFile(@NotNull final FileManager fileManager, @NotNull final Path path) {
        this.fileManager = fileManager;
        this.path = path;
    }

    public abstract @NotNull C loadConfig() throws IOException;

    public @NotNull C getConfiguration() {
        return this.configuration;
    }

    public @NotNull I load() {
        if (isDirectory()) return (I) this;

        final Path path = getPath();

        if (!hasAction(FileAction.ALREADY_EXTRACTED)) {
            if (hasAction(FileAction.EXTRACT_FILE)) {
                this.fileManager.extractFile(path);
            }

            if (hasAction(FileAction.EXTRACT_FOLDER)) {
                this.fileManager.extractFolder(path.getFileName().toString(), path.getParent());
            }
        }

        this.configuration = CompletableFuture.supplyAsync(() -> {
            try {
                return loadConfig();
            } catch (final IOException exception) {
                throw new FileException("Failed to load file %s".formatted(getPath()), exception);
            }
        }).join();

        return (I) this;
    }

    public @NotNull I save(@NotNull final String content) {
        if (isDirectory()) return (I) this;

        if (this.configuration == null) return (I) this;

        CompletableFuture.runAsync(() -> {
            try {
                saveConfig(content);
            } catch (final IOException exception) {
                throw new FileException("Failed to save content for %s".formatted(getPath()), exception);
            }
        });

        return (I) this;
    }

    public @NotNull I save() {
        return save("");
    }

    public void saveConfig(@NotNull final String content) throws IOException {
        saveConfig();
    }

    public void setOptions(@NotNull final Consumer<O> options) {
        options.accept(this.options);
    }

    public void removeAction(@NotNull final FileAction action) {
        this.actions.remove(action);
    }

    public boolean hasAction(@NotNull final FileAction action) {
        return this.actions.contains(action);
    }

    public void addAction(@NotNull final FileAction action) {
        this.actions.add(action);
    }

    public void setLoader(@NotNull final L loader) {
        this.loader = loader;
    }

    public void saveConfig() throws IOException {

    }

    public @NotNull String getPrettyName() {
        return getFileName().replace(getFileType().getExtension(), "");
    }

    public @NotNull FileType getFileType() {
        return this.fileType;
    }

    public @NotNull String getFileName() {
        return this.path.getFileName().toString();
    }

    public boolean isDirectory() {
        return Files.isDirectory(getPath());
    }

    public @NotNull Path getPath() {
        return this.path;
    }

    public @NotNull O getOptions() {
        return this.options;
    }

    public boolean isLoaded() {
        return Files.exists(this.path);
    }
}