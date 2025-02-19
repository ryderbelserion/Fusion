package com.ryderbelserion.fusion.core.files;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.migration.MigrationService;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.core.FusionLayout;
import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.api.enums.FileType;
import com.ryderbelserion.fusion.core.api.exception.FusionException;
import com.ryderbelserion.fusion.core.files.types.JaluCustomFile;
import com.ryderbelserion.fusion.core.files.types.JsonCustomFile;
import com.ryderbelserion.fusion.core.files.types.misc.LogCustomFile;
import com.ryderbelserion.fusion.core.files.types.misc.NbtCustomFile;
import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import com.ryderbelserion.fusion.core.util.FileUtils;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationOptions;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public class FileManager {

    private final FusionLayout api = FusionProvider.get();

    private final ComponentLogger logger = this.api.getLogger();

    private final boolean isVerbose = this.api.isVerbose();

    private final File dataFolder = this.api.getDataFolder();

    private final Map<String, CustomFile<? extends CustomFile<?>>> files = new HashMap<>();

    public FileManager() {}

    public final FileManager init() { // only initialize once
        return this;
    }

    public final FileManager addFolder(@NotNull final String folder, @NotNull final FileType fileType, @Nullable final UnaryOperator<ConfigurationOptions> options) {
        if (folder.isBlank()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot add the folder as the folder is empty.");
            }

            return this;
        }

        final File directory = new File(this.dataFolder, folder);

        if (directory.mkdirs()) {
            FileUtils.extracts(String.format("/%s/", directory.getName()), directory.toPath(), false);
        }

        final File[] contents = directory.listFiles();

        if (contents == null) return this;

        final String extension = fileType.getExtension();

        for (final File file : contents) {
            if (file.isDirectory()) {
                final String[] files = file.list();

                if (files == null) continue;

                for (final String fileName : files) {
                    if (fileType != FileType.NONE && !fileName.endsWith("." + extension)) continue; // just in case people are weird

                    addFile(fileName, folder + File.separator + file.getName(), fileType, options, true);
                }

                continue;
            }

            final String fileName = file.getName();

            if (fileType != FileType.NONE && !fileName.endsWith("." + extension)) continue; // just in case people are weird

            addFile(fileName, folder, fileType, options, true);
        }

        return this;
    }

    // no extraction required as this is only for ConfigMe
    @SafeVarargs
    public final FileManager addFile(@NotNull final String fileName, @Nullable final MigrationService service, @Nullable final YamlFileResourceOptions options, @NotNull final Class<? extends SettingsHolder>... holders) {
        final File file = new File(this.dataFolder, fileName);

        final JaluCustomFile jalu = new JaluCustomFile(file, holders);

        if (service != null) {
            jalu.setService(service);
        }

        if (options != null) {
            jalu.setOptions(options);
        }

        this.files.put(strip(fileName, ".yml"), jalu.build());

        return this;
    }

    public final FileManager addFile(@NotNull final String fileName, @Nullable final String folder, @NotNull final FileType fileType, @Nullable final UnaryOperator<ConfigurationOptions> defaultOptions, final boolean isDynamic) {
        if (fileName.isBlank()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot add the file as the file is null or empty.");
            }

            return this;
        }

        final File file = new File(this.dataFolder, folder != null ? folder + File.separator + fileName : fileName);

        if (!file.exists()) {
            extractFile(file.getPath());
        }

        final String extension = fileType.getExtension();
        final String strippedName = strip(fileName, extension);

        switch (fileType) {
            case YAML -> {
                if (this.files.containsKey(strippedName)) {
                    this.files.get(strippedName).load();

                    return this;
                }

                this.files.put(strippedName, new YamlCustomFile(file, isDynamic, defaultOptions).load());
            }

            case JSON -> {
                if (this.files.containsKey(strippedName)) {
                    this.files.get(strippedName).load();

                    return this;
                }

                this.files.put(strippedName, new JsonCustomFile(file, isDynamic, defaultOptions).load());
            }

            case NBT -> {
                if (this.files.containsKey(strippedName)) {
                    throw new FusionException("The file '" + strippedName + "' already exists.");
                }

                this.files.put(strippedName, new NbtCustomFile(file, isDynamic));
            }

            case LOG -> {
                if (this.files.containsKey(strippedName)) {
                    throw new FusionException("The file '" + strippedName + "' already exists.");
                }

                if (folder == null) {
                    throw new FusionException("You must specify a folder for the " + extension + " file.");
                }

                this.files.put(strippedName, new LogCustomFile(file, new File(this.dataFolder, folder), isDynamic));
            }

            case NONE -> {}
        }

        return this;
    }

    public final FileManager addFile(@NotNull final String fileName, @NotNull final FileType fileType, @Nullable final UnaryOperator<ConfigurationOptions> options) {
        return addFile(fileName, null, fileType, options, false);
    }

    public final FileManager addFile(@NotNull final String fileName, @NotNull final FileType fileType) {
        return addFile(fileName, fileType, null);
    }

    public final FileManager addFile(@NotNull final String fileName) {
        FileType fileType = FileType.NONE;

        if (fileName.endsWith(".yml")) {
            fileType = FileType.YAML;
        } else if (fileName.endsWith(".json")) {
            fileType = FileType.JSON;
        } else if (fileName.endsWith(".nbt")) {
            fileType = FileType.NBT;
        }

        return addFile(fileName, null, fileType, null, false);
    }

    public final FileManager saveFile(@NotNull final String fileName) {
        FileType fileType = FileType.NONE;

        if (fileName.endsWith(".yml")) {
            fileType = FileType.YAML;
        } else if (fileName.endsWith(".json")) {
            fileType = FileType.JSON;
        } else if (fileName.endsWith(".nbt")) {
            fileType = FileType.NBT;
        }

        return saveFile(fileName, fileType);
    }

    public final FileManager saveFile(@NotNull final String fileName, @NotNull final FileType fileType) {
        if (fileName.isBlank()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot save the file as the file is null or empty.");
            }

            return this;
        }

        final String extension = fileType.getExtension();

        final String strippedName = strip(fileName, extension);

        if (!this.files.containsKey(strippedName)) {
            if (this.isVerbose) {
                this.logger.warn("Cannot save the file as the file does not exist.");
            }

            return this;
        }

        this.files.get(strippedName).save();

        return this;
    }

    // write to the log file
    public final FileManager writeFile(@NotNull final String fileName, @NotNull final FileType fileType, @NotNull final String content) {
        if (fileType != FileType.LOG) {
            throw new FusionException("The file '" + fileName + "' is not a log file.");
        }

        if (fileName.isBlank()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot write to the file as the file is null or empty.");
            }

            return this;
        }

        final String extension = fileType.getExtension();

        final String strippedName = strip(fileName, extension);

        if (!this.files.containsKey(strippedName)) {
            if (this.isVerbose) {
                this.logger.warn("Cannot write to file as the file does not exist.");
            }

            return this;
        }

        this.files.get(strippedName).write(content);

        return this;
    }

    // removes a file with an option to delete
    public final FileManager removeFile(@NotNull final String fileName, @NotNull final FileType fileType, final boolean purge) {
        if (fileName.isBlank()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot remove the file as the file is null or empty.");
            }

            return this;
        }

        final String strippedName = strip(fileName, fileType.getExtension());

        if (!this.files.containsKey(strippedName)) return this;

        final CustomFile<? extends CustomFile<?>> file = this.files.remove(strippedName);

        if (purge) {
            file.delete();

            return this;
        }

        file.save();

        return this;
    }

    public final FileManager removeFile(final CustomFile<? extends CustomFile<?>> customFile, final boolean purge) {
        return removeFile(customFile.getFileName(), customFile.getType(), purge);
    }

    public final FileManager reload() { // reloads all files
        return handle(false);
    }

    public final FileManager save() { // saves all files
        return handle(true);
    }

    public final FileManager purge() {
        this.files.clear();

        return this;
    }

    // Extracts a resource to a specific path.
    public final FileManager extractResource(@NotNull final String resource, @NotNull final String output, boolean replaceExisting) {
        final Path path = this.dataFolder.toPath();

        FileUtils.extracts(String.format(File.separator + "%s" + File.separator, resource), path.resolve(output), replaceExisting);

        return this;
    }

    // Extracts a folder without loading a file into the map.
    public final FileManager extractFolder(@NotNull final String folder) {
        final File directory = new File(this.dataFolder, folder);

        if (directory.mkdirs()) {
            FileUtils.extracts(String.format(File.separator + "%s" + File.separator, directory.getName()), directory.toPath(), false);
        }

        return this;
    }

    // Extracts a file without loading a file into the map.
    public final FileManager extractFile(@NotNull final String name) {
        final File file = new File(this.dataFolder, name);

        if (file.exists()) return this;

        FileUtils.saveResource(name, false, this.isVerbose);

        return this;
    }

    private FileManager handle(boolean toggle) { // save or reload all files
        if (this.files.isEmpty()) return this;

        final List<File> brokenFiles = new ArrayList<>();

        for (final Map.Entry<String, CustomFile<? extends CustomFile<?>>> entry : this.files.entrySet()) {
            final CustomFile<? extends CustomFile<?>> customFile = entry.getValue();

            final File file = customFile.getFile();

            if (!file.exists()) {
                brokenFiles.add(file);

                continue;
            }

            if (toggle) {
                customFile.save(); // save the config
            } else {
                customFile.load(); // load the config
            }
        }

        if (!brokenFiles.isEmpty()) { // remove broken files
            brokenFiles.forEach(file -> {
                final String fileName = file.getName();

                this.files.remove(fileName);
            });
        }

        return this;
    }

    public @Nullable CustomFile<? extends CustomFile<?>> getFile(final String fileName, final FileType fileType) {
        return this.files.getOrDefault(strip(fileName, fileType.getExtension()), null);
    }

    public @Nullable JaluCustomFile getJaluFile(final String fileName) {
        return (JaluCustomFile) this.files.getOrDefault(strip(fileName, FileType.JALU.getExtension()), null);
    }

    public final String strip(final String fileName, final String extension) {
        return fileName.replace(extension, "");
    }

    public Map<String, CustomFile<? extends CustomFile<?>>> getFiles() {
        return this.files;
    }
}