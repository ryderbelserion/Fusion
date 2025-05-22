package com.ryderbelserion.fusion.core.files;

import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.core.FusionCore;
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
import java.util.logging.Logger;

public class FileManager {

    private final FusionCore api = FusionCore.Provider.get();
    private final Logger logger = this.api.getLogger();
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

    public void init(@NotNull final List<FileAction> actions) {
        this.dataFolder.toFile().mkdirs();

        this.folders.forEach((folder, type) -> addFolder(folder, type, actions, null));
    }

    // ConfigMe
    public void addFolder(@NotNull final Path folder, @NotNull final Consumer<SettingsManagerBuilder> builder, @NotNull final List<FileAction> actions, @Nullable final YamlFileResourceOptions options) {
        addFolder(folder, FileType.JALU);

        extractFolder(folder, new ArrayList<>(actions) {{
            add(FileAction.EXTRACT);
        }});

        final List<Path> files = FileUtils.getFiles(this.dataFolder.resolve(folder), ".yml", this.depth);

        for (final Path path : files) {
            addFile(path, builder, actions, options);
        }
    }

    public void addFolder(@NotNull final Path folder, @NotNull final FileType fileType, @NotNull final List<FileAction> actions, @Nullable final UnaryOperator<ConfigurationOptions> options) {
        addFolder(folder, fileType);

        extractFolder(folder, actions);

        final List<Path> files = FileUtils.getFiles(folder, fileType.getExtension(), this.depth);

        for (final Path path : files) {
            addFile(path, actions, options);
        }
    }

    public void addFolder(@NotNull final Path folder, @NotNull final FileType fileType) {
        this.folders.putIfAbsent(folder, fileType);
    }

    // ConfigMe
    public void addFile(@NotNull final Path path, @NotNull final Consumer<SettingsManagerBuilder> builder, @NotNull final List<FileAction> actions, @Nullable final YamlFileResourceOptions options) {
        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.getOrDefault(path, null);

        if (file != null && !actions.contains(FileAction.RELOAD)) {
            file.load();

            return;
        }

        final JaluCustomFile jalu = new JaluCustomFile(path, builder, actions, options);

        this.customFiles.putIfAbsent(path, jalu.load());
    }

    public void addFile(@NotNull final Path path, @NotNull final List<FileAction> actions, @Nullable final UnaryOperator<ConfigurationOptions> options) {
        final String fileName = path.getFileName().toString();

        if (actions.contains(FileAction.EXTRACT) && !Files.exists(path)) {
            FileUtils.extract(fileName, path.getParent(), actions);
        }

        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.getOrDefault(path, null);

        if (file != null && !actions.contains(FileAction.RELOAD)) {
            file.load();

            return;
        }

        final FileType fileType = detectFileType(fileName);

        ICustomFile<? extends ICustomFile<?>> customFile = null;

        switch (fileType) {
            case YAML -> customFile = new YamlCustomFile(path, actions, options).load();
            case JSON -> customFile = new JsonCustomFile(path, actions, options).load();
            case LOG -> customFile = new LogCustomFile(path, actions).load();
            case JALU -> customFile = new JaluCustomFile(path, settingsManagerBuilder -> {

            }, actions).load();
            default -> {}
        }

        if (customFile != null) {
            this.customFiles.putIfAbsent(path, customFile);
        }
    }

    public void saveFile(@NotNull final Path path, @NotNull final List<FileAction> actions, @NotNull final String content) {
        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.getOrDefault(path, null);

        if (file == null) {
            if (this.isVerbose) {
                this.logger.warning("Cannot write to file as the file does not exist.");
            }

            return;
        }

        if (file.getFileType() != FileType.LOG) {
            if (this.isVerbose) {
                this.logger.warning("The file " + file.getFileName() + " is not a log file.");
            }

            return;
        }

        file.save(content, actions);
    }

    public void saveFile(@NotNull final Path path) {
        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.getOrDefault(path, null);

        if (file == null) {
            if (this.isVerbose) {
                this.logger.warning("Cannot write to file as the file does not exist.");
            }

            return;
        }

        if (file.getFileType() == FileType.LOG) {
            if (this.isVerbose) {
                this.logger.warning("Please use the correct method FileManager#saveFile(path, content) to write to log files!");
            }

            return;
        }

        file.save();
    }

    public final void removeFile(@NotNull final Path path, final FileAction action) {
        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.get(path);

        if (action == FileAction.DELETE) {
            file.delete();

            this.customFiles.remove(path);

            return;
        }

        file.save();
    }

    public void purge() {
        this.customFiles.clear();
        this.folders.clear();
    }

    public final void removeFile(@NotNull final ICustomFile<? extends ICustomFile<?>> customFile, final FileAction action) {
        removeFile(customFile.getPath(), action);
    }

    public final void extractFolder(@NotNull final Path folder, @NotNull final List<FileAction> actions) {
        FileUtils.extract(folder.getFileName().toString(), this.dataFolder, new ArrayList<>(actions) {{
            add(FileAction.FOLDER);
        }});
    }

    public void extractResource(@NotNull final String path, @NotNull final String output, @NotNull final FileAction action) {
        FileUtils.extract(path, this.dataFolder.resolve(output), new ArrayList<>() {{
            add(action);
        }});
    }

    public void extractResource(@NotNull final String path) {
        FileUtils.extract(path, this.dataFolder, new ArrayList<>());
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

    public void refresh(final boolean save) { // save or reload all files
        if (this.customFiles.isEmpty()) return;

        // folder -> file object
        // we search hashmap by the folder, fetch hashmap of folder, remove file from hashmap of folder and then re-set the hashmap
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
    }
}