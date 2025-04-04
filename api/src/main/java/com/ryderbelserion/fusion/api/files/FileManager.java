package com.ryderbelserion.fusion.api.files;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.migration.MigrationService;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.api.FusionApi;
import com.ryderbelserion.fusion.api.enums.FileType;
import com.ryderbelserion.fusion.api.exceptions.FusionException;
import com.ryderbelserion.fusion.api.interfaces.ILogger;
import com.ryderbelserion.fusion.api.files.types.JaluCustomFile;
import com.ryderbelserion.fusion.api.files.types.JsonCustomFile;
import com.ryderbelserion.fusion.api.files.types.misc.LogCustomFile;
import com.ryderbelserion.fusion.api.files.types.misc.NbtCustomFile;
import com.ryderbelserion.fusion.api.files.types.YamlCustomFile;
import com.ryderbelserion.fusion.api.utils.FileUtils;
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
import java.util.Optional;
import java.util.function.UnaryOperator;

public class FileManager { // note: no longer strip file names, so it's stored as config.yml or crates.log

    private final FusionApi api = FusionApi.Provider.get();

    private final ILogger logger = this.api.getLogger();

    private final boolean isVerbose = this.api.isVerbose();

    private final Path dataFolder = this.api.getDataFolder();

    private final int depth = this.api.getDepth();

    private final Map<String, FileType> fileMap = Map.of(
            ".yml", FileType.YAML,
            ".json", FileType.JSON,
            ".nbt", FileType.NBT,
            ".log", FileType.LOG
    );

    // folder -> file name -> associated custom file
    // this can also be used to replace files above, which means we use getFile#getPath()
    private final Map<Path, CustomFile<? extends CustomFile<?>>> customFiles = new HashMap<>();
    private final Map<Path, FileType> folders = new HashMap<>(); // stores the folder and it's file type

    public final FileManager init(final boolean isReload) {
        this.dataFolder.toFile().mkdirs();
        
        this.folders.forEach((folder, type) -> addFolder(folder, type, Optional.empty(), isReload)); // add new files

        return this;
    }

    public final FileManager addFolder(@NotNull final Path folder, @NotNull final FileType fileType, @NotNull final Optional<UnaryOperator<ConfigurationOptions>> options, final boolean isReload) {
        this.folders.putIfAbsent(folder, fileType);

        FileUtils.extract(folder.getFileName().toString(), this.dataFolder, false);

        final List<Path> files = FileUtils.getFiles(folder, fileType.getExtension(), this.depth);

        for (final Path path : files) {
            addFile(path, options, true, isReload);
        }

        return this;
    }

    public final FileManager addFolder(@NotNull final Path folder, @NotNull final FileType fileType) {
        this.folders.putIfAbsent(folder, fileType);

        return this;
    }

    // extraction is required, but this sets up configme for folders
    @SafeVarargs
    public final FileManager addFolder(@NotNull final Path folder, @NotNull final Optional<MigrationService> service, @NotNull final Optional<YamlFileResourceOptions> options, final boolean isReload, final boolean isDynamic, @NotNull final Class<? extends SettingsHolder>... holders) {
        this.folders.putIfAbsent(folder, FileType.YAML);

        FileUtils.extract(folder.getFileName().toString(), this.dataFolder, false);

        final List<Path> files = FileUtils.getFiles(this.dataFolder.resolve(folder), ".yml", this.depth);

        for (final Path path : files) {
            addFile(path, service, options, isReload, isDynamic, holders);
        }

        return this;
    }

    // no extraction required as this is only for ConfigMe
    @SafeVarargs
    public final FileManager addFile(@NotNull final Path path, @NotNull final Optional<MigrationService> service, @NotNull final Optional<YamlFileResourceOptions> options, final boolean isReload, final boolean isDynamic, @NotNull final Class<? extends SettingsHolder>... holders) {
        final CustomFile<? extends CustomFile<?>> file = this.customFiles.getOrDefault(path, null);

        if (file != null && !isReload) {
            file.load();

            return this;
        }

        final JaluCustomFile jalu = new JaluCustomFile(path, isDynamic, holders);

        service.ifPresent(jalu::setService);
        options.ifPresent(jalu::setOptions);

        // only add to map if absent
        this.customFiles.putIfAbsent(path, jalu.build());

        return this;
    }

    public FileManager addFile(@NotNull final Path path, @NotNull final Optional<UnaryOperator<ConfigurationOptions>> options, final boolean isDynamic, final boolean isReload) {
        final String fileName = path.getFileName().toString();

        if (!Files.exists(path)) {
            FileUtils.extract(fileName, path.getParent(), false);
        }

        final FileType fileType = detectFileType(fileName);

        final CustomFile<? extends CustomFile<?>> file = this.customFiles.getOrDefault(path, null);

        if (file != null && !isReload) {
            file.load();

            return this;
        }

        switch (fileType) {
            case YAML -> this.customFiles.putIfAbsent(path, new YamlCustomFile(path, isDynamic, options).load());

            case JSON -> this.customFiles.putIfAbsent(path, new JsonCustomFile(path, isDynamic, options).load());

            case NBT -> this.customFiles.putIfAbsent(path, new NbtCustomFile(path, isDynamic));

            case LOG -> this.customFiles.putIfAbsent(path, new LogCustomFile(path, isDynamic));

            case NONE -> {}
        }

        return this;
    }

    public final FileManager addFile(@NotNull final Path path, @NotNull final Optional<UnaryOperator<ConfigurationOptions>> options) {
        return addFile(path, options, false, false);
    }

    public final FileManager addFile(@NotNull final Path path) {
        return addFile(path, Optional.empty());
    }

    public final FileManager saveFile(@NotNull final Path path) {
        final CustomFile<? extends CustomFile<?>> file = this.customFiles.getOrDefault(path, null);

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

        final CustomFile<? extends CustomFile<?>> file = this.customFiles.getOrDefault(path, null);

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
        final CustomFile<? extends CustomFile<?>> file = this.customFiles.get(path);

        if (purge) {
            file.delete();

            this.customFiles.remove(path);

            return this;
        }

        file.save();

        return this;
    }

    public final FileManager removeFile(@NotNull final CustomFile<? extends CustomFile<?>> customFile, final boolean purge) {
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

    // Extracts a resource to a specific path.
    public final FileManager extractResource(@NotNull final String path, @NotNull final String output, boolean purge) {
        FileUtils.extract(path, this.dataFolder.resolve(output), purge);

        return this;
    }

    // Extracts a resource to a specific path
    public final FileManager extractResource(@NotNull final String path) {
        FileUtils.extract(path, this.dataFolder, false);

        return this;
    }

    private FileManager refresh(boolean toggle) { // save or reload all files
        if (this.customFiles.isEmpty()) return this;

        // folder -> file object
        // we search hashmap by the folder, fetch hashmap of folder, remove file from hashmap of folder and then re-set the hashmap
        final List<Path> brokenFiles = new ArrayList<>();

        for (Map.Entry<Path, CustomFile<? extends CustomFile<?>>> file : this.customFiles.entrySet()) {
            final CustomFile<? extends CustomFile<?>> value = file.getValue();

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

    public @Nullable final CustomFile<? extends CustomFile<?>> getCustomFile(@NotNull final Path path) {
        return getCustomFiles().getOrDefault(path, null);
    }

    public @Nullable final YamlCustomFile getYamlFile(final Path path) {
        return (YamlCustomFile) getCustomFile(path);
    }

    public @Nullable final JsonCustomFile getJsonFile(final Path path) {
        return (JsonCustomFile) getCustomFile(path);
    }

    public @Nullable final JaluCustomFile getJaluFile(final Path path) {
        return (JaluCustomFile) getCustomFile(path);
    }

    public @Nullable final LogCustomFile getLogFile(final Path path) {
        return (LogCustomFile) getCustomFile(path);
    }

    public Map<Path, CustomFile<? extends CustomFile<?>>> getCustomFiles() {
        return Collections.unmodifiableMap(this.customFiles);
    }
}