package com.ryderbelserion.core.files;

import com.ryderbelserion.core.FusionLayout;
import com.ryderbelserion.core.FusionProvider;
import com.ryderbelserion.core.api.enums.FileType;
import com.ryderbelserion.core.api.exception.FusionException;
import com.ryderbelserion.core.files.types.JsonCustomFile;
import com.ryderbelserion.core.files.types.NbtCustomFile;
import com.ryderbelserion.core.files.types.YamlCustomFile;
import com.ryderbelserion.core.util.FileMethods;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileManager {

    protected final FusionLayout api = FusionProvider.get();

    protected final ComponentLogger logger = this.api.getLogger();

    protected final boolean isVerbose = this.api.isVerbose();

    private final File dataFolder = this.api.getDataFolder();

    private final Map<String, CustomFile<? extends CustomFile<?>>> files = new HashMap<>();

    public FileManager() {}

    public final FileManager addFolder(@NotNull final String folder, @NotNull final FileType fileType) {
        if (folder.isBlank()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot add the folder as the folder is empty.");
            }

            return this;
        }

        final File directory = new File(this.dataFolder, folder);

        if (directory.mkdirs()) {
            FileMethods.extracts(FileManager.class, String.format("/%s/", directory.getName()), directory.toPath(), false);
        }

        final File[] contents = directory.listFiles();

        if (contents == null) return this;

        final String extension = fileType.getExtension();

        for (final File file : contents) {
            if (file.isDirectory()) {
                final String[] files = file.list();

                if (files == null) continue;

                for (final String fileName : files) {
                    if (!fileName.endsWith("." + extension)) continue; // just in case people are weird

                    addFile(fileName, folder + File.separator + file.getName(), true, fileType);
                }

                continue;
            }

            final String fileName = file.getName();

            if (!fileName.endsWith("." + extension)) continue; // just in case people are weird

            addFile(fileName, folder, true, fileType);
        }

        return this;
    }

    public final FileManager addFile(@NotNull final String fileName) {
        return addFile(fileName, null, false, FileType.NONE);
    }

    public final FileManager addFile(@NotNull final String fileName, @NotNull final FileType fileType) {
        return addFile(fileName, null, false, fileType);
    }

    public final FileManager addFile(@NotNull final String fileName, @Nullable final String folder, final boolean isDynamic, @NotNull final FileType fileType) {
        if (fileName.isBlank()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot add the file as the file is null or empty.");
            }

            return this;
        }

        final String extension = fileType.getExtension();

        final String strippedName = strip(fileName, extension);

        final File file = new File(this.dataFolder, folder != null ? folder + File.separator + fileName : fileName);

        if (!file.exists()) {
            if (this.isVerbose) {
                this.logger.warn("Successfully extracted file {} to {}", fileName, file.getPath());
            }

            FileMethods.saveResource(folder == null ? fileName : folder + File.separator + fileName, false, this.isVerbose);
        }

        switch (fileType) {
            case YAML -> {
                if (this.files.containsKey(strippedName)) {
                    this.files.get(strippedName).loadConfiguration();

                    return this;
                }

                this.files.put(strippedName, new YamlCustomFile(file, isDynamic).loadConfiguration());
            }

            case JSON -> {
                if (this.files.containsKey(strippedName)) {
                    this.files.get(strippedName).loadConfiguration();

                    return this;
                }

                this.files.put(strippedName, new JsonCustomFile(file, isDynamic).loadConfiguration());
            }

            case NBT -> {
                if (this.files.containsKey(strippedName)) {
                    throw new FusionException("The file '" + strippedName + "' already exists.");
                }

                this.files.put(strippedName, new NbtCustomFile(file, isDynamic));
            }

            case NONE -> {} // do nothing
        }

        return this;
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

        this.files.get(strippedName).saveConfiguration();

        return this;
    }

    public final FileManager removeFile(final CustomFile<? extends CustomFile<?>> customFile, final boolean purge) {
        return removeFile(customFile.getFileName(), customFile.getFileType(), purge);
    }

    public final FileManager removeFile(@NotNull final String fileName, @NotNull final FileType fileType, final boolean purge) {
        if (fileName.isBlank()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot remove the file as the file is null or empty.");
            }

            return this;
        }

        final String strippedName = strip(fileName, fileType.getExtension());

        if (!this.files.containsKey(strippedName)) return this;

        final CustomFile<? extends CustomFile<?>> customFile = this.files.remove(fileName);

        if (purge) {
            customFile.deleteConfiguration();

            return this;
        }

        customFile.saveConfiguration();

        return this;
    }

    public FileManager reloadFiles() {
        final List<String> forRemoval = new ArrayList<>();

        this.files.forEach((name, file) -> {
            if (file.getFile().exists()) {
                file.loadConfiguration();
            } else {
                forRemoval.add(name);
            }
        });

        forRemoval.forEach(this.files::remove);

        if (this.isVerbose && !forRemoval.isEmpty()) {
            this.logger.info("{} file(s) were removed from cache, because they did not exist.", forRemoval.size());
        }

        return this;
    }

    public FileManager purge() {
        this.files.clear();

        return this;
    }

    public @Nullable CustomFile<? extends CustomFile<?>> getFile(final String fileName, final FileType fileType) {
        return this.files.getOrDefault(strip(fileName, fileType.getExtension()), null);
    }

    public final String strip(final String fileName, final String extension) {
        return fileName.replace("." + extension, "");
    }

    public Map<String, CustomFile<? extends CustomFile<?>>> getFiles() {
        return this.files;
    }
}