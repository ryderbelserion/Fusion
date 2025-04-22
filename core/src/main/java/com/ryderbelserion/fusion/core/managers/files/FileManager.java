package com.ryderbelserion.fusion.core.managers.files;

import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.interfaces.ICustomFile;
import com.ryderbelserion.fusion.core.managers.files.types.JaluCustomFile;
import com.ryderbelserion.fusion.core.managers.files.types.LogCustomFile;
import com.ryderbelserion.fusion.core.managers.files.types.YamlCustomFile;
import com.ryderbelserion.fusion.core.utils.FileUtils;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
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

public class FileManager { // note: no longer strip file names, so it's stored as config.yml or crates.log

    private final FusionCore api = FusionCore.Provider.get();

    private final ComponentLogger logger = this.api.getLogger();

    private final boolean isVerbose = this.api.isVerbose();

    private final Path dataFolder = this.api.getDataPath();

    private final int depth = this.api.getRecursionDepth();

    private final Map<String, FileType> fileMap = Map.of(
            ".yml", FileType.YAML,
            //".json", FileType.JSON,
            ".log", FileType.LOG
    );

    // folder -> file name -> associated custom file
    // this can also be used to replace files above, which means we use getFile#getPath()
    private final Map<Path, ICustomFile<? extends ICustomFile<?>>> customFiles = new HashMap<>();
    private final Map<Path, FileType> folders = new HashMap<>(); // stores the folder and it's file type

    public final FileManager init(final boolean isReload, final boolean isExtract) {
        this.dataFolder.toFile().mkdirs();

        this.folders.forEach((folder, type) -> addFolder(folder, type, null, isReload, isExtract)); // add new files

        return this;
    }

    public final FileManager init(final boolean isReload) {
        return init(isReload, true);
    }

    public FileManager addFolder(@NotNull final Path folder, @NotNull final FileType fileType, @Nullable final UnaryOperator<ConfigurationOptions> options, final boolean isReload, final boolean isExtract) {
        this.folders.putIfAbsent(folder, fileType);

        extractFolder(folder, false, isExtract);

        final List<Path> files = FileUtils.getFiles(folder, fileType.getExtension(), this.depth);

        for (final Path path : files) {
            addFile(path, options, false, isReload, isExtract);
        }

        return this;
    }

    public FileManager addFolder(@NotNull final Path folder, @NotNull final FileType fileType, @Nullable final UnaryOperator<ConfigurationOptions> options, final boolean isReload) {
        return addFolder(folder, fileType, options, isReload, true);
    }

    public final FileManager addFolder(@NotNull final Path folder, @NotNull final FileType fileType) {
        this.folders.putIfAbsent(folder, fileType);

        return this;
    }

    // extraction is required, but this sets up configme for folders
    public FileManager addFolder(@NotNull final Path folder, @NotNull final Consumer<SettingsManagerBuilder> builder, @Nullable final YamlFileResourceOptions options, final boolean isReload, final boolean isStatic) {
        this.folders.putIfAbsent(folder, FileType.YAML);

        extractFolder(folder, false, true);

        final List<Path> files = FileUtils.getFiles(this.dataFolder.resolve(folder), ".yml", this.depth);

        for (final Path path : files) {
            addFile(path, builder, options, isReload, isStatic);
        }

        return this;
    }

    // no extraction required as this is only for ConfigMe
    public FileManager addFile(@NotNull final Path path, @NotNull final Consumer<SettingsManagerBuilder> builder, @Nullable final YamlFileResourceOptions options, final boolean isReload, final boolean isStatic) {
        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.getOrDefault(path, null);

        if (file != null && !isReload) {
            file.load();

            return this;
        }

        final JaluCustomFile jalu = new JaluCustomFile(path, builder, options, isStatic);

        // only add to map if absent
        this.customFiles.putIfAbsent(path, jalu.build());

        return this;
    }

    public FileManager addFile(@NotNull final Path path, @Nullable final UnaryOperator<ConfigurationOptions> options, final boolean isStatic, final boolean isReload, final boolean isExtract) {
        final String fileName = path.getFileName().toString();

        if (isExtract && !Files.exists(path)) {
            FileUtils.extract(fileName, path.getParent(), false, false);
        }

        final FileType fileType = detectFileType(fileName);

        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.getOrDefault(path, null);

        if (file != null && !isReload) {
            file.load();

            return this;
        }

        switch (fileType) {
            case YAML -> this.customFiles.putIfAbsent(path, new YamlCustomFile(path, options, isStatic).load());

            case LOG -> this.customFiles.putIfAbsent(path, new LogCustomFile(path, isStatic));

            case NONE -> {}
        }

        return this;
    }

    public final FileManager addFile(@NotNull final Path path, @Nullable final UnaryOperator<ConfigurationOptions> options) {
        return addFile(path, options, false, false, true);
    }

    public final FileManager addFile(@NotNull final Path path) {
        return addFile(path, null);
    }

    public final FileManager saveFile(@NotNull final Path path) {
        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.getOrDefault(path, null);

        if (file == null) {
            return this;
        }

        final String fileName = path.getFileName().toString();

        final FileType fileType = detectFileType(fileName);

        if (fileType == FileType.LOG) {
            throw new FusionException("You must use FileManager#writeFile since the FileType is set to LOG");
        }

        file.save();

        return this;
    }

    // write to the log file
    public final FileManager writeFile(@NotNull final Path path, @NotNull final String content) {
        final String fileName = path.getFileName().toString();

        final FileType fileType = detectFileType(fileName);

        if (fileType == FileType.LOG) {
            throw new FusionException("The file " + fileName + " is not a log file.");
        }

        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.getOrDefault(path, null);

        if (file == null) {
            if (this.isVerbose) {
                this.logger.warn("Cannot write to file as the file does not exist.");
            }

            return this;
        }

        file.write(content);

        return this;
    }

    // removes a file with an option to delete
    public final FileManager removeFile(@NotNull final Path path, final boolean purge) {
        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.get(path);

        if (purge) {
            file.delete();

            this.customFiles.remove(path);

            return this;
        }

        file.save();

        return this;
    }

    public final FileManager removeFile(@NotNull final ICustomFile<? extends ICustomFile<?>> customFile, final boolean purge) {
        return removeFile(customFile.getPath(), purge);
    }

    public final FileManager reload() { // reloads all files
        return refresh(false);
    }

    public final FileManager save() { // saves all files
        return refresh(true);
    }

    public final FileManager purge() {
        this.customFiles.clear();

        return this;
    }

    // Extracts multiple folders
    public final FileManager extractFolder(@NotNull final List<Path> folders, final boolean purge, final boolean isExtract) {
        if (!isExtract) return this;

        for (final Path path : folders) {
            extractFolder(path, purge, true);
        }

        return this;
    }

    // Extracts an entire folder
    public final FileManager extractFolder(@NotNull final Path folder, final boolean purge, final boolean isExtract) {
        if (!isExtract) return this;

        FileUtils.extract(folder.getFileName().toString(), this.dataFolder, true, purge);

        return this;
    }

    // Extracts a resource to a specific path.
    public final FileManager extractResource(@NotNull final String path, @NotNull final String output, final boolean purge) {
        FileUtils.extract(path, this.dataFolder.resolve(output), false, purge);

        return this;
    }

    // Extracts a resource to a specific path
    public final FileManager extractResource(@NotNull final String path) {
        FileUtils.extract(path, this.dataFolder, false, false);

        return this;
    }

    private FileManager refresh(final boolean toggle) { // save or reload all files
        if (this.customFiles.isEmpty()) return this;

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

            if (toggle) {
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

    public FileType detectFileType(@NotNull final String fileName) {
        return this.fileMap.entrySet().stream().filter(entry -> fileName.endsWith(entry.getKey())).map(Map.Entry::getValue).findFirst().orElse(FileType.NONE);
    }

    public @Nullable final ICustomFile<? extends ICustomFile<?>> getCustomFile(@NotNull final Path path) {
        return getCustomFiles().getOrDefault(path, null);
    }

    public @Nullable final YamlCustomFile getYamlFile(@NotNull final Path path) {
        return (YamlCustomFile) getCustomFile(path);
    }

    public @Nullable final JaluCustomFile getJaluFile(@NotNull final Path path) {
        return (JaluCustomFile) getCustomFile(path);
    }

    public @Nullable final LogCustomFile getLogFile(@NotNull final Path path) {
        return (LogCustomFile) getCustomFile(path);
    }

    public @NotNull final Map<Path, ICustomFile<? extends ICustomFile<?>>> getCustomFiles() {
        return Collections.unmodifiableMap(this.customFiles);
    }
}