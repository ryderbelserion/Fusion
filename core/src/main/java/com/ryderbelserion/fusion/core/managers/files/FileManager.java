package com.ryderbelserion.fusion.core.managers.files;

import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.interfaces.ICustomFile;
import com.ryderbelserion.fusion.core.api.enums.FileType;
import com.ryderbelserion.fusion.core.api.objects.FileKey;
import com.ryderbelserion.fusion.core.api.registry.types.FileRegistry;
import com.ryderbelserion.fusion.core.managers.files.types.JaluCustomFile;
import com.ryderbelserion.fusion.core.managers.files.types.YamlCustomFile;
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
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class FileManager { // note: no longer strip file names, so it's stored as config.yml or crates.log

    private final FusionCore api = FusionCore.Provider.get();

    private final Path dataFolder = this.api.getDataPath();

    private final int depth = this.api.getRecursionDepth();

    // folder -> file name -> associated custom file
    // this can also be used to replace files above, which means we use getFile#getPath()
    private final Map<Path, ICustomFile<? extends ICustomFile<?>>> customFiles = new HashMap<>();
    private final Map<Path, FileType> folders = new HashMap<>(); // stores the folder and it's file type

    private final FileRegistry registry;

    public FileManager(@NotNull FileRegistry registry) {
        this.registry = registry;
    }

    public FileManager init(final boolean isReload, final boolean isExtract) {
        this.dataFolder.toFile().mkdirs();
        
        this.folders.forEach((folder, type) -> addFolder(folder, type, null, isReload, isExtract)); // add new files

        return this;
    }

    public FileManager init(final boolean isReload) {
        return init(isReload, true);
    }

    public FileManager addFolder(@NotNull final Path folder, @NotNull final FileType fileType, @Nullable final UnaryOperator<ConfigurationOptions> options, final boolean isReload, final boolean isExtract) {
        this.folders.putIfAbsent(folder, fileType);

        extractFolder(folder, false, isExtract);

        final List<Path> files = FileUtils.getFiles(folder, fileType.getExtension(), this.depth);

        for (final Path path : files) {
            addFile(path, options, isReload, isExtract);
        }

        return this;
    }

    public FileManager addFolder(@NotNull final Path folder, @NotNull final FileType fileType, @Nullable final UnaryOperator<ConfigurationOptions> options, final boolean isReload) {
        return addFolder(folder, fileType, options, isReload, true);
    }

    public FileManager addFolder(@NotNull final Path folder, @NotNull final FileType fileType) {
        this.folders.putIfAbsent(folder, fileType);

        return this;
    }

    // extraction is required, but this sets up configme for folders
    public FileManager addFolder(@NotNull final Path folder, @NotNull final Consumer<SettingsManagerBuilder> builder, @Nullable final YamlFileResourceOptions options, final boolean isReload, final boolean isDynamic) {
        this.folders.putIfAbsent(folder, FileType.YAML);

        extractFolder(folder, false, true);

        final List<Path> files = FileUtils.getFiles(this.dataFolder.resolve(folder), ".yml", this.depth);

        for (final Path path : files) {
            addFile(path, builder, options, isReload, isDynamic);
        }

        return this;
    }

    // no extraction required as this is only for ConfigMe
    public FileManager addFile(@NotNull final Path path, @NotNull final Consumer<SettingsManagerBuilder> builder, @Nullable final YamlFileResourceOptions options, final boolean isReload, final boolean isDynamic) {
        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.getOrDefault(path, null);

        if (file != null && !isReload) {
            file.load();

            return this;
        }

        final JaluCustomFile jalu = new JaluCustomFile(path, builder, options, isDynamic);

        // only add to map if absent
        this.customFiles.putIfAbsent(path, jalu.build());

        return this;
    }

    public FileManager addFile(@NotNull final Path path, @NotNull final Consumer<SettingsManagerBuilder> builder, final boolean isReload, final boolean isDynamic) {
        return addFile(path, builder, null, isReload, isDynamic);
    }

    public FileManager addFile(@NotNull final Path path, @Nullable final UnaryOperator<ConfigurationOptions> options, final boolean isReload, final boolean isExtract) {
        final String fileName = path.getFileName().toString();

        if (isExtract && !Files.exists(path)) {
            FileUtils.extract(fileName, path.getParent(), false);
        }

        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.getOrDefault(path, null);

        if (file != null && !isReload) {
            file.load();

            return this;
        }

        final Optional<FileKey> fileKey = detectFileKey(fileName);



        /*switch (fileType) {
            //case YAML -> this.customFiles.putIfAbsent(path, new YamlCustomFile(path, isDynamic, options).load());

            //case JSON -> this.customFiles.putIfAbsent(path, new JsonCustomFile(path, isDynamic, options).load());

            case NONE -> {}
        }*/

        return this;
    }

    /*public FileManager addFile(@NotNull final Path path, @NotNull final Optional<UnaryOperator<ConfigurationOptions>> options) {
        return addFile(path, options, false, false, true);
    }*/

    /*public FileManager addFile(@NotNull final Path path) {
        return addFile(path, Optional.empty());
    }*/

    // removes a file with an option to delete
    public FileManager removeFile(@NotNull final Path path, final boolean purge) {
        final ICustomFile<? extends ICustomFile<?>> file = this.customFiles.get(path);

        if (purge) {
            file.delete();

            this.customFiles.remove(path);

            return this;
        }

        file.save();

        return this;
    }

    public FileManager removeFile(@NotNull final ICustomFile<? extends ICustomFile<?>> customFile, final boolean purge) {
        return removeFile(customFile.getPath(), purge);
    }

    public FileManager reload() { // reloads all files
        return refresh(false);
    }

    public FileManager save() { // saves all files
        return refresh(true);
    }

    public final FileManager purge() {
        this.customFiles.clear();

        return this;
    }

    public FileManager extractFolder(@NotNull final List<Path> folders, final boolean purge, final boolean isExtract) {
        if (!isExtract) return this;

        for (final Path path : folders) {
            extractFolder(path, purge, true);
        }

        return this;
    }

    public FileManager extractFolder(@NotNull final Path folder, final boolean purge, final boolean isExtract) {
        if (!isExtract) return this;

        FileUtils.extract(folder.getFileName().toString(), this.dataFolder, purge);

        return this;
    }

    public FileManager extractResource(@NotNull final String path, @NotNull final String output, boolean purge) {
        FileUtils.extract(path, this.dataFolder.resolve(output), purge);

        return this;
    }

    public FileManager extractResource(@NotNull final String path) {
        FileUtils.extract(path, this.dataFolder, false);

        return this;
    }

    private FileManager refresh(boolean toggle) { // save or reload all files
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

    public Optional<FileKey> detectFileKey(@NotNull String fileName) {
        return this.registry.getRegistry().values().stream().filter(entry -> fileName.endsWith(entry.getExtension())).findFirst();
    }

    public @Nullable ICustomFile<? extends ICustomFile<?>> getCustomFile(@NotNull final Path path) {
        return getCustomFiles().getOrDefault(path, null);
    }

    public @Nullable YamlCustomFile getYamlFile(final Path path) {
        return (YamlCustomFile) getCustomFile(path);
    }

    public @Nullable JaluCustomFile getJaluFile(final Path path) {
        return (JaluCustomFile) getCustomFile(path);
    }

    public Map<Path, ICustomFile<? extends ICustomFile<?>>> getCustomFiles() {
        return Collections.unmodifiableMap(this.customFiles);
    }
}