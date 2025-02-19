package com.ryderbelserion.fusion.core.files.v2;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.migration.MigrationService;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.core.FusionLayout;
import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.api.enums.FileType;
import com.ryderbelserion.fusion.core.files.v2.types.JaluCustomFile;
import com.ryderbelserion.fusion.core.util.FileUtils;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseFileManager {

    private final FusionLayout api = FusionProvider.get();

    private final ComponentLogger logger = this.api.getLogger();

    private final boolean isVerbose = this.api.isVerbose();

    private final File dataFolder = this.api.getDataFolder();

    private final Map<String, BaseCustomFile<? extends BaseCustomFile<?>>> files = new HashMap<>();

    public BaseFileManager() {}

    public final BaseFileManager init() { // only initialize once
        return this;
    }

    // no extraction required as this is only for ConfigMe
    @SafeVarargs
    public final BaseFileManager addFile(@NotNull final String fileName, @Nullable final MigrationService service, @Nullable final YamlFileResourceOptions options, @NotNull final Class<? extends SettingsHolder>... holders) {
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

    // removes a file with an option to delete
    public final BaseFileManager removeFile(@NotNull final String fileName, @NotNull final FileType fileType, final boolean purge) {
        if (fileName.isBlank()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot remove the file as the file is null or empty.");
            }

            return this;
        }

        final String strippedName = strip(fileName, fileType.getExtension());

        if (!this.files.containsKey(strippedName)) return this;

        final BaseCustomFile<? extends BaseCustomFile<?>> file = this.files.remove(strippedName);

        if (purge) {
            file.delete();

            return this;
        }

        file.save();

        return this;
    }

    public final BaseFileManager reload() { // reloads all files
        return handle(false);
    }

    public final BaseFileManager save() { // saves all files
        return handle(true);
    }

    // Extracts a resource to a specific path.
    public final BaseFileManager extractResource(@NotNull final String resource, @NotNull final String output, boolean replaceExisting) {
        final Path path = this.dataFolder.toPath();

        FileUtils.extracts(String.format(File.separator + "%s" + File.separator, resource), path.resolve(output), replaceExisting);

        return this;
    }

    // Extracts a folder without loading a file into the map.
    public final BaseFileManager extractFolder(@NotNull final String folder) {
        final File directory = new File(this.dataFolder, folder);

        if (directory.mkdirs()) {
            FileUtils.extracts(String.format(File.separator + "%s" + File.separator, directory.getName()), directory.toPath(), false);
        }

        return this;
    }

    // Extracts a file without loading a file into the map.
    public final BaseFileManager extractFile(@NotNull final String name) {
        final File file = new File(this.dataFolder, name);

        if (file.exists()) return this;

        FileUtils.saveResource(name, false, this.isVerbose);

        return this;
    }

    private BaseFileManager handle(boolean toggle) { // save or reload all files
        if (this.files.isEmpty()) return this;

        final List<File> brokenFiles = new ArrayList<>();

        for (final Map.Entry<String, BaseCustomFile<? extends BaseCustomFile<?>>> entry : this.files.entrySet()) {
            final BaseCustomFile<? extends BaseCustomFile<?>> customFile = entry.getValue();

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

    public @Nullable BaseCustomFile<? extends BaseCustomFile<?>> getFile(final String fileName, final FileType fileType) {
        return this.files.getOrDefault(strip(fileName, fileType.getExtension()), null);
    }

    public @Nullable JaluCustomFile getJaluFile(final String fileName) {
        return (JaluCustomFile) this.files.getOrDefault(strip(fileName, FileType.JALU.getExtension()), null);
    }

    public final String strip(final String fileName, final String extension) {
        return fileName.replace(extension, "");
    }
}