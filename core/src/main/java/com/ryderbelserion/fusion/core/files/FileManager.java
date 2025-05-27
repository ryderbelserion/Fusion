package com.ryderbelserion.fusion.core.files;

import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
import com.ryderbelserion.fusion.core.files.types.JaluCustomFile;
import com.ryderbelserion.fusion.core.api.interfaces.ICustomFile;
import com.ryderbelserion.fusion.core.files.types.JsonCustomFile;
import com.ryderbelserion.fusion.core.files.types.LogCustomFile;
import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import com.ryderbelserion.fusion.core.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationOptions;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class FileManager {

    private final FusionCore api = FusionCore.Provider.get();
    private final ILogger logger = this.api.getLogger();
    private final boolean isVerbose = this.api.isVerbose();
    private final Path dataFolder = this.api.getPath();
    private final int depth = this.api.getRecursionDepth();

    private final Map<Path, ICustomFile<? extends ICustomFile<?>>> customFiles = new HashMap<>();
    private final Map<Path, FileType> folders = new HashMap<>();

    private final Map<String, FileType> fileMap = Map.of(
            ".yml", FileType.YAML,
            ".json", FileType.JSON,
            ".log", FileType.LOG
    );

    public FileManager init(@NotNull final List<FileAction> actions) {
        this.dataFolder.toFile().mkdirs();

        this.folders.forEach((folder, type) -> addFolder(folder, type, actions, null));

        return this;
    }

    // ConfigMe
    public FileManager addFolder(@NotNull final Path folder, @NotNull final Consumer<SettingsManagerBuilder> builder, @NotNull final List<FileAction> actions, @Nullable final YamlFileResourceOptions options) {
        addFolder(folder, FileType.JALU);

        extractFolder(folder, new ArrayList<>(actions) {{
            add(FileAction.EXTRACT);
        }});

        final List<Path> files = FileUtils.getFiles(this.dataFolder.resolve(folder), ".yml", this.depth);

        for (final Path path : files) {
            addFile(path, builder, actions, options);
        }

        return this;
    }

    public FileManager addFolder(@NotNull final Path folder, @NotNull final FileType fileType, @NotNull final List<FileAction> actions, @Nullable final UnaryOperator<ConfigurationOptions> options) {
        addFolder(folder, fileType);

        extractFolder(folder, actions);

        final List<Path> files = FileUtils.getFiles(folder, fileType.getExtension(), this.depth);

        for (final Path path : files) {
            addFile(path, actions, options);
        }

        return this;
    }

    public FileManager addFolder(@NotNull final Path folder, @NotNull final FileType fileType) {
        this.folders.putIfAbsent(folder, fileType);

        return this;
    }

    // ConfigMe
    public FileManager addFile(@NotNull final Path path, @NotNull final Consumer<SettingsManagerBuilder> builder, @NotNull final List<FileAction> actions, @Nullable final YamlFileResourceOptions options) {
        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.getOrDefault(path, null);

        if (file != null && !actions.contains(FileAction.RELOAD)) { // if the reload action is not present, we load the file!
            file.load();

            return this;
        }

        final JaluCustomFile jalu = new JaluCustomFile(path, builder, actions, options);

        this.customFiles.putIfAbsent(path, jalu.load());

        return this;
    }

    public FileManager addFile(@NotNull final Path path, @NotNull final List<FileAction> actions, @Nullable final UnaryOperator<ConfigurationOptions> options) {
        final String fileName = path.getFileName().toString();

        if (actions.contains(FileAction.EXTRACT) && !Files.exists(path)) {
            FileUtils.extract(fileName, path.getParent(), actions);
        }

        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.getOrDefault(path, null);

        if (file != null && !actions.contains(FileAction.RELOAD)) { // if the reload action is not present, we load the file!
            file.load();

            return this;
        }

        final FileType fileType = detectFileType(fileName);

        ICustomFile<? extends ICustomFile<?>> customFile = null;

        switch (fileType) {
            case YAML -> customFile = new YamlCustomFile(path, actions, options).load();
            case JSON -> customFile = new JsonCustomFile(path, actions, options).load();
            case LOG -> customFile = new LogCustomFile(path, actions).load();
            default -> {}
        }

        if (customFile != null) {
            this.customFiles.putIfAbsent(path, customFile);
        }

        return this;
    }

    public FileManager saveFile(@NotNull final Path path, @NotNull final List<FileAction> actions, @NotNull final String content) {
        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.getOrDefault(path, null);

        if (file == null) {
            if (this.isVerbose) {
                this.logger.warn("Cannot write to file as the file does not exist.");
            }

            return this;
        }

        if (file.getFileType() != FileType.LOG) {
            if (this.isVerbose) {
                this.logger.warn("The file {} is not a log file.", file.getFileName());
            }

            return this;
        }

        file.save(content, actions);

        return this;
    }

    public FileManager saveFile(@NotNull final Path path) {
        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.getOrDefault(path, null);

        if (file == null) {
            if (this.isVerbose) {
                this.logger.warn("Cannot write to file as the file does not exist.");
            }

            return this;
        }

        if (file.getFileType() == FileType.LOG) {
            if (this.isVerbose) {
                this.logger.warn("Please use the correct method FileManager#saveFile(path, content) to write to log files!");
            }

            return this;
        }

        file.save();

        return this;
    }

    public final FileManager removeFile(@NotNull final Path path, final FileAction action) {
        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.get(path);

        if (action == FileAction.DELETE) {
            file.delete();

            this.customFiles.remove(path);

            return this;
        }

        file.save();

        return this;
    }

    public FileManager purge() {
        this.customFiles.clear();
        this.folders.clear();

        return this;
    }

    public final FileManager removeFile(@NotNull final ICustomFile<? extends ICustomFile<?>> customFile, final FileAction action) {
        removeFile(customFile.getPath(), action);

        return this;
    }

    public final FileManager extractFolder(@NotNull final Path folder, @NotNull final List<FileAction> actions) {
        FileUtils.extract(folder.getFileName().toString(), this.dataFolder, new ArrayList<>(actions) {{
            add(FileAction.FOLDER);
        }});

        return this;
    }

    public FileManager extractResource(@NotNull final String path, @NotNull final String output, @NotNull final FileAction action) {
        FileUtils.extract(path, this.dataFolder.resolve(output), new ArrayList<>() {{
            add(action);
        }});

        return this;
    }

    public FileManager extractResource(@NotNull final String path) {
        FileUtils.extract(path, this.dataFolder, new ArrayList<>());

        return this;
    }

    public FileType detectFileType(@NotNull final String fileName) {
        return this.fileMap.entrySet().stream().filter(entry -> fileName.endsWith(entry.getKey())).map(Map.Entry::getValue).findFirst().orElse(FileType.NONE);
    }

    public @Nullable final ICustomFile<? extends ICustomFile<?>> getCustomFile(@NotNull final Path path) {
        return getCustomFiles().getOrDefault(path, null);
    }

    public @Nullable final YamlCustomFile getYamlFile(@NotNull final Path path) {
        return (YamlCustomFile) getCustomFile(path);
    }

    public @Nullable final JsonCustomFile getJsonFile(@NotNull final Path path) {
        return (JsonCustomFile) getCustomFile(path);
    }

    public @Nullable final JaluCustomFile getJaluFile(@NotNull final Path path) {
        return (JaluCustomFile) getCustomFile(path);
    }

    public @NotNull final Map<Path, ICustomFile<? extends ICustomFile<?>>> getCustomFiles() {
        return Collections.unmodifiableMap(this.customFiles);
    }

    public FileManager refresh(final boolean save) { // save or reload all files
        if (this.customFiles.isEmpty()) return this;

        final List<Path> brokenFiles = new ArrayList<>();

        for (Map.Entry<Path, ICustomFile<? extends ICustomFile<?>>> file : this.customFiles.entrySet()) {
            final ICustomFile<? extends ICustomFile<?>> value = file.getValue();

            if (value == null) {
                continue;
            }

            final Path path = value.getPath();

            if (!Files.exists(path)) {
                brokenFiles.add(path);

                continue;
            }

            if (save) {
                value.save(); // save the config
            } else {
                value.load(); // load the config
            }
        }

        if (!brokenFiles.isEmpty()) {
            brokenFiles.forEach(this.customFiles::remove);
        }

        return this;
    }
}