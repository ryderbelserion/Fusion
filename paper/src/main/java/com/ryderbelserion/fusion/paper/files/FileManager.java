package com.ryderbelserion.fusion.paper.files;

import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.core.api.enums.FileAction;
import com.ryderbelserion.fusion.core.api.enums.FileType;
import com.ryderbelserion.fusion.core.api.interfaces.IFileManager;
import com.ryderbelserion.fusion.core.api.interfaces.files.ICustomFile;
import com.ryderbelserion.fusion.core.api.utils.FileUtils;
import com.ryderbelserion.fusion.core.files.types.JaluCustomFile;
import com.ryderbelserion.fusion.core.files.types.LogCustomFile;
import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import com.ryderbelserion.fusion.paper.files.types.NbtCustomFile;
import com.ryderbelserion.fusion.paper.files.types.PaperCustomFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationOptions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Loads, reloads, or saves existing files. It supports ConfigMe, Configurate-Jackson/Yaml,
 * and even supports loading/saving log files... It uses the {@link Path} as an identifier.
 */
public class FileManager extends IFileManager {

    protected final Map<Path, ICustomFile<? extends ICustomFile<?>>> customFiles = new HashMap<>();
    protected final Map<Path, FileType> folders = new HashMap<>();

    /**
     * Loads all folders manually added using FileManager#addFolder, usually used when you don't care to specify what you want.
     *
     * @param actions a list of actions to define what to do
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager init(@NotNull final List<FileAction> actions) {
        if (!Files.exists(this.path)) {
            try {
                Files.createDirectory(path);
            } catch (final IOException exception) {
                this.fusion.log("error", "Failed to create {}! Exception: {}", this.path, exception.getMessage());
            }
        }

        this.folders.forEach((folder, type) -> addFolder(folder, type, actions, null));

        return this;
    }

    /**
     * Adds a folder which will be mapping ConfigMe.
     *
     * @param folder the folder to extract/map
     * @param builder the object mapped classes for the configs
     * @param actions a list of actions to define what to do
     * @param options optional options to configure indentation size etc
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager addFolder(@NotNull final Path folder, @NotNull final Consumer<SettingsManagerBuilder> builder, @NotNull final List<FileAction> actions, @Nullable final YamlFileResourceOptions options) {
        this.folders.put(folder, FileType.JALU);

        extractFolder(folder, new ArrayList<>(actions) {{
            add(FileAction.EXTRACT_FOLDER);
        }});

        final List<Path> files = FileUtils.getFiles(this.path.resolve(folder), ".yml", this.fusion.getDepth());

        for (final Path path : files) {
            addFile(path, builder, actions, options);
        }

        return this;
    }

    /**
     * Adds a folder which will be mapping ConfigMe.
     *
     * @param folder the folder to extract/map
     * @param builder the object mapped classes for the configs
     * @param actions a list of actions to define what to do
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager addFolder(@NotNull final Path folder, @NotNull final Consumer<SettingsManagerBuilder> builder, @NotNull final List<FileAction> actions) {
        return addFolder(folder, builder, actions, null);
    }

    /**
     * Adds a folder which will be mapping ConfigMe.
     *
     * @param folder the folder to extract/map
     * @param builder the object mapped classes for the configs
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager addFolder(@NotNull final Path folder, @NotNull final Consumer<SettingsManagerBuilder> builder) {
        return addFolder(folder, builder, new ArrayList<>());
    }

    /**
     * Adds a folder which is used to map Configurate yaml/jackson.
     *
     * @param folder the folder to extract/map
     * @param actions a list of actions to define what to do
     * @param options optional options to configure indentation size etc
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager addFolder(@NotNull final Path folder, @NotNull final FileType fileType, @NotNull final List<FileAction> actions, @Nullable final UnaryOperator<ConfigurationOptions> options) {
        this.folders.put(folder, fileType);

        extractFolder(folder, actions);

        final List<Path> files = FileUtils.getFiles(folder, fileType.getExtension(), this.fusion.getDepth());

        for (final Path path : files) {
            addFile(path, fileType, actions, options);
        }

        return this;
    }

    /**
     * Adds a folder to a hashmap.
     *
     * @param folder the folder to add
     * @param fileType the type of file expected in the folder
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager addFolder(@NotNull final Path folder, @NotNull final FileType fileType) {
        addFolder(folder, fileType, new ArrayList<>(), null);

        return this;
    }

    /**
     * Adds a path which will be mapping ConfigMe or reload if already present.
     *
     * @param path the path to extract/map
     * @param builder the object mapped classes for the configs
     * @param actions a list of actions to define what to do
     * @param options optional options to configure indentation size etc
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager addFile(@NotNull final Path path, @NotNull final Consumer<SettingsManagerBuilder> builder, @NotNull final List<FileAction> actions, @Nullable final YamlFileResourceOptions options) {
        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.getOrDefault(path, null);

        if (file != null && !actions.contains(FileAction.RELOAD_FILE)) { // if the reload action is not present, we load the file!
            file.load();

            return this;
        }

        final JaluCustomFile jalu = new JaluCustomFile(path, builder, actions, options);

        this.customFiles.putIfAbsent(path, jalu.load());

        return this;
    }

    /**
     * Adds a path which will be mapping ConfigMe or reload if already present.
     *
     * @param path the path to extract/map
     * @param builder the object mapped classes for the configs
     * @param actions a list of actions to define what to do
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager addFile(@NotNull final Path path, @NotNull final Consumer<SettingsManagerBuilder> builder, @NotNull final List<FileAction> actions) {
        return addFile(path, builder, actions, null);
    }

    /**
     * Adds a path which will be mapping ConfigMe or reload if already present.
     *
     * @param path the path to extract/map
     * @param builder the object mapped classes for the configs
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager addFile(@NotNull final Path path, @NotNull final Consumer<SettingsManagerBuilder> builder) {
        return addFile(path, builder, new ArrayList<>());
    }

    /**
     * Adds a folder which is used to map Configurate yaml/jackson or log files. It automatically determines the file type!
     *
     * @param path the path to extract/map
     * @param fileType the type of file expected in the folder
     * @param actions a list of actions to define what to do
     * @param options optional options to configure indentation size etc
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager addFile(@NotNull final Path path, @NotNull final FileType fileType, @NotNull final List<FileAction> actions, @Nullable final UnaryOperator<ConfigurationOptions> options) {
        final String fileName = path.getFileName().toString();

        if (!Files.exists(path)) { // always extract if it does not exist.
            FileUtils.extract(fileName, path.getParent(), actions);
        }

        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.getOrDefault(path, null);

        if (file != null && !actions.contains(FileAction.RELOAD_FILE)) { // if the reload action is not present, we load the file!
            file.load();

            return this;
        }

        ICustomFile<? extends ICustomFile<?>> customFile = null;

        switch (fileType) {
            case CONFIGURATE -> customFile = new YamlCustomFile(path, actions, options).load();
            case PAPER -> customFile = new PaperCustomFile(path, actions).load();
            case LOG -> customFile = new LogCustomFile(path, actions).load();
            case NBT -> customFile = new NbtCustomFile(path, actions).load();

            default -> {}
        }

        if (customFile != null) {
            this.customFiles.putIfAbsent(path, customFile);
        }

        return this;
    }

    /**
     * Adds a folder which is used to map Configurate yaml/jackson or log files. It automatically determines the file type!
     *
     * @param path the path to extract/map
     * @param fileType the type of file expected in the folder
     * @param actions a list of actions to define what to do
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager addFile(@NotNull final Path path, @NotNull final FileType fileType, @NotNull final List<FileAction> actions) {
        return addFile(path, fileType, actions, null);
    }

    /**
     * Adds a folder which is used to map Configurate yaml/jackson or log files. It automatically determines the file type!
     *
     * @param path the path to extract/map
     * @param fileType the type of file expected in the folder
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager addFile(@NotNull final Path path, @NotNull final FileType fileType) {
        return addFile(path, fileType, new ArrayList<>(), null);
    }

    /**
     * Adds a custom file to the hashmap.
     *
     * @param customFile {@link ICustomFile}
     * @return {@link IFileManager}
     */
    @Override
    public @NotNull IFileManager addFile(@NotNull final ICustomFile customFile) {
        this.customFiles.putIfAbsent(customFile.getPath(), customFile);

        return this;
    }

    /**
     * Saves contents at the given path i.e. a log file.
     *
     * @param path the path
     * @param actions a list of actions to define what to do
     * @param content the content to save to the log file
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager saveFile(@NotNull final Path path, @NotNull final List<FileAction> actions, @NotNull final String content) {
        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.getOrDefault(path, null);

        if (file == null) {
            this.fusion.log("warn", "Cannot write to file as the file does not exist.");

            return this;
        }

        if (file.getFileType() != FileType.LOG) {
            this.fusion.log("warn", "The file {} is not a log file.", file.getFileName());

            return this;
        }

        file.save(content, actions);

        return this;
    }

    /**
     * Saves contents at a given path.
     *
     * @param path the path
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager saveFile(@NotNull final Path path) {
        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.getOrDefault(path, null);

        if (file == null) {
            this.fusion.log("warn", "Cannot write to file as the file does not exist.");

            return this;
        }

        if (file.getFileType() == FileType.LOG) {
            this.fusion.log("warn", "Please use the correct method FileManager#saveFile(path, content) to write to log files!");

            return this;
        }

        file.save();

        return this;
    }

    /**
     * Removes a file from the cache with the option to delete if the {@link FileAction} is specified.
     *
     * @param path the path
     * @param action the action
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager removeFile(@NotNull final Path path, @Nullable final FileAction action) {
        if (!this.customFiles.containsKey(path)) return this;

        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.get(path);

        if (action == FileAction.DELETE_FILE) {
            file.delete();
        }

        if (action == FileAction.DELETE_CACHE) {
            this.customFiles.remove(path);

            return this;
        }

        file.save();

        return this;
    }

    /**
     * Removes a file from the cache.
     *
     * @param path the path
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager removeFile(@NotNull final Path path) {
        return removeFile(path, null);
    }

    /**
     * Removes a file using {@link ICustomFile}.
     *
     * @param customFile the custom file
     * @param action the action to specify
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager removeFile(@NotNull final ICustomFile<? extends ICustomFile<?>> customFile, @Nullable final FileAction action) {
        removeFile(customFile.getPath(), action);

        return this;
    }

    /**
     * Removes a file using {@link ICustomFile}.
     *
     * @param customFile the custom file
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager removeFile(@NotNull final ICustomFile<? extends ICustomFile<?>> customFile) {
        return removeFile(customFile, null);
    }

    /**
     * Purges all file data without saving or reloading.
     *
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager purge() {
        this.customFiles.clear();
        this.folders.clear();

        return this;
    }

    /**
     * Extracts a folder with a specified list of actions.
     *
     * @param folder the folder to extract
     * @param actions the list of actions to define what to do.
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager extractFolder(@NotNull final Path folder, @NotNull final List<FileAction> actions) {
        actions.add(FileAction.EXTRACT_FOLDER);

        FileUtils.extract(folder.getFileName().toString(), this.path, actions);

        return this;
    }

    /**
     * Extracts from a path to the output folder.
     *
     * @param path the input path
     * @param output the output path
     * @param action the action type
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager extractResource(@NotNull final String path, @NotNull final String output, @NotNull final FileAction action) {
        FileUtils.extract(path, this.path.resolve(output), new ArrayList<>() {{
            add(action);
        }});

        return this;
    }

    /**
     * Extracts a single resource to a specific path.
     *
     * @param path the input/output
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager extractResource(@NotNull final String path) {
        FileUtils.extract(path, this.path, new ArrayList<>());

        return this;
    }

    /**
     * Fetches a generic custom file.
     *
     * @param path the path in the cache
     * @return {@link ICustomFile}
     */
    @Override
    public @Nullable final ICustomFile<? extends ICustomFile<?>> getCustomFile(@NotNull final Path path) {
        return getCustomFiles().getOrDefault(path, null);
    }

    /**
     * Fetches a {@link PaperCustomFile} from the cache.
     *
     * @param path the path in the cache
     * @return {@link PaperCustomFile}
     */
    public @Nullable final PaperCustomFile getPaperCustomFile(@NotNull final Path path) {
        return (PaperCustomFile) getCustomFile(path);
    }

    /**
     * Fetches a {@link NbtCustomFile} from the cache.
     *
     * @param path the path in the cache
     * @return {@link NbtCustomFile}
     */
    public @Nullable final NbtCustomFile getNbtFile(@NotNull final Path path) {
        return (NbtCustomFile) getCustomFile(path);
    }

    /**
     * Fetches a {@link LogCustomFile} from the cache.
     *
     * @param path the path in the cache
     * @return {@link LogCustomFile}
     */
    public @Nullable final LogCustomFile getLogFile(@NotNull final Path path) {
        return (LogCustomFile) getCustomFile(path);
    }

    /**
     * Fetches a {@link YamlCustomFile} from the cache.
     *
     * @param path the path in the cache
     * @return {@link YamlCustomFile}
     */
    public @Nullable final YamlCustomFile getConfigurateFile(@NotNull final Path path) {
        return (YamlCustomFile) getCustomFile(path);
    }

    /**
     * Fetches a {@link JaluCustomFile} from the cache.
     *
     * @param path the path in the cache
     * @return {@link JaluCustomFile}
     */
    public @Nullable final JaluCustomFile getJaluFile(@NotNull final Path path) {
        return (JaluCustomFile) getCustomFile(path);
    }

    /**
     * Fetches all existing custom files.
     *
     * @return an unmodifiable map of custom files
     */
    @Override
    public @NotNull final Map<Path, ICustomFile<? extends ICustomFile<?>>> getCustomFiles() {
        return Collections.unmodifiableMap(this.customFiles);
    }

    /**
     * Saves or reloads all files.
     *
     * @param save true or false
     * @return {@link FileManager}
     */
    @Override
    public @NotNull final FileManager refresh(final boolean save) { // save or reload all files
        if (this.customFiles.isEmpty()) return this;

        final List<Path> brokenFiles = new ArrayList<>();

        for (final Map.Entry<Path, ICustomFile<? extends ICustomFile<?>>> file : this.customFiles.entrySet()) {
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