package com.ryderbelserion.fusion.files.interfaces;

import com.ryderbelserion.fusion.files.FileException;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.yaml.NodeStyle;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

// I i.e. YamlCustomFile.
// C i.e. the SettingsManager, or CommentedConfigurationNode/BasicConfigurationNode.
// L i.e. the SettingsManagerBuilder, or YamlConfigurationLoader.
// O i.e. options that configure the functionality of L.
public abstract class ICustomFile<I, C, L> {

    protected final List<FileAction> actions = new ArrayList<>();

    protected ConfigurationOptions options = ConfigurationOptions.defaults();
    protected C configuration;
    protected L loader;

    protected FileManager fileManager;
    protected FileType fileType;
    protected String jarFolder;
    protected Path path;

    public ICustomFile(
            @NonNull final FileManager fileManager,
            @NonNull final String jarFolder,
            @NonNull final Path path
    ) {
        this.fileManager = fileManager;
        this.jarFolder = jarFolder;
        this.path = path;
    }

    public ICustomFile(
            @NonNull final FileManager fileManager,
            @NonNull final Path path
    ) {
        this(fileManager, "", path);
    }

    protected HeaderMode headerMode = HeaderMode.PRESERVE;
    protected NodeStyle nodeStyle = NodeStyle.BLOCK;
    protected boolean hasComments = true;
    protected boolean isLenient = true;
    protected int indent = 2;

    public abstract @NonNull C loadConfig() throws IOException;

    public void withIndent(final int indent) {
        this.indent = indent;
    }

    public void withLenient(final boolean isLenient) {
        this.isLenient = isLenient;
    }

    public void withComments(final boolean hasComments) {
        this.hasComments = hasComments;
    }

    public void withNodeStyle(final NodeStyle nodeStyle) {
        this.nodeStyle = nodeStyle;
    }

    public void withHeaderMode(final HeaderMode headerMode) {
        this.headerMode = headerMode;
    }

    public @NonNull C getConfiguration() {
        return this.configuration;
    }

    public @NonNull I load() {
        if (isDirectory()) return (I) this;

        final Path path = getPath();
        final Path parent = path.getParent();

        final String input = path.getFileName().toString();

        if (!hasAction(FileAction.ALREADY_EXTRACTED)) {
            if (hasAction(FileAction.EXTRACT_FILE)) {
                this.fileManager.extractFile(input);
            }

            if (hasAction(FileAction.EXTRACT_FOLDER)) {
                this.fileManager.extractFolder(input, this.jarFolder, this.fileType, parent);
            }
        }

        this.configuration = CompletableFuture.supplyAsync(() -> {
            try {
                return loadConfig();
            } catch (final IOException exception) {
                throw new FileException("Failed to load file %s".formatted(path), exception);
            }
        }).join();

        if (hasAction(FileAction.RELOAD_FILE)) { // config loaded, we remove!
            removeAction(FileAction.RELOAD_FILE);
        }

        return (I) this;
    }

    public @NonNull I save(@NonNull final String content) {
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

    public @NonNull I save() {
        return save("");
    }

    public void saveConfig(@NonNull final String content) throws IOException {
        saveConfig();
    }

    public void setOptions(@NonNull final UnaryOperator<ConfigurationOptions> options) {
        options.apply(this.options);
    }

    public void removeAction(@NonNull final FileAction action) {
        this.actions.remove(action);
    }

    public boolean hasAction(@NonNull final FileAction action) {
        return this.actions.contains(action);
    }

    public void addAction(@NonNull final FileAction action) {
        this.actions.add(action);
    }

    public void setLoader(@NonNull final L loader) {
        this.loader = loader;
    }

    public void saveConfig() throws IOException {

    }

    public @NonNull ConfigurationOptions getOptions() {
        return this.options;
    }

    public @NonNull String getPrettyName() {
        return getFileName().replace(getFileType().getExtension(), "");
    }

    public @NonNull FileType getFileType() {
        return this.fileType;
    }

    public @NonNull String getFileName() {
        return this.path.getFileName().toString();
    }

    public boolean isDirectory() {
        return Files.isDirectory(getPath());
    }

    public @NonNull Path getPath() {
        return this.path;
    }

    public boolean isLoaded() {
        return Files.exists(this.path);
    }
}