package com.ryderbelserion.paper.files;

import com.ryderbelserion.paper.FusionApi;
import com.ryderbelserion.core.api.enums.FileType;
import com.ryderbelserion.core.api.exception.FusionException;
import com.ryderbelserion.paper.Fusion;
import com.ryderbelserion.core.util.FileUtils;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class FileManager {

    private final FusionApi fusionApi = FusionApi.get();

    private final Fusion api = this.fusionApi.getFusion();
    private final ComponentLogger logger = this.api.getLogger();
    private final File dataFolder = this.api.getDataFolder();
    private final boolean isVerbose = this.api.isVerbose();

    private final Map<String, CustomFile> files = new HashMap<>();

    private final Map<String, FileType> folders = new HashMap<>();

    public FileManager() {}

    public FileManager addFolder(@NotNull final String folder, @NotNull final FileType fileType) {
        if (folder.isBlank()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot add the folder as the folder is empty.");
            }

            return this;
        }

        if (!this.folders.containsKey(folder)) {
            this.folders.put(folder, fileType);
        }

        final File directory = new File(this.dataFolder, folder);

        if (!directory.exists()) {
            directory.mkdirs();

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

    public FileManager addFile(@NotNull final CustomFile customFile) {
        this.files.put(customFile.getEffectiveName(), customFile);
        
        return this;
    }

    public FileManager addFile(@NotNull final String fileName) {
        FileType type = FileType.NONE;

        if (fileName.endsWith(".yml")) {
            type = FileType.YAML;
        } else if (fileName.endsWith(".json")) {
            type = FileType.JSON;
        } else if (fileName.endsWith(".nbt")) {
            type = FileType.NBT;
        }

        return addFile(fileName, null, false, type);
    }

    public FileManager addFile(@NotNull final String fileName, @NotNull final FileType fileType) {
        return addFile(fileName, null, false, fileType);
    }

    public FileManager addFile(@NotNull final String fileName, @Nullable final String folder, final boolean isDynamic, @NotNull final FileType fileType) {
        if (fileName.isBlank()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot add the file as the file is null or empty.");
            }

            return this;
        }

        final String extension = fileType.getExtension();

        final String strippedName = strip(fileName, extension);

        final String resourcePath = folder != null ? folder + File.separator + fileName : fileName;

        final File file = new File(this.dataFolder, resourcePath);

        if (!file.exists()) {
            if (this.isVerbose) {
                this.logger.warn("Successfully extracted file {} to {}", fileName, file.getPath());
            }

            FileUtils.saveResource(resourcePath, false, this.isVerbose);
        }

        switch (fileType) {
            case NBT -> {
                if (this.files.containsKey(strippedName)) {
                    throw new FusionException("The file '" + strippedName + "' already exists.");
                }

                this.files.put(strippedName, new CustomFile(fileType, file, isDynamic));
            }

            case YAML -> {
                if (this.files.containsKey(strippedName)) {
                    this.files.get(strippedName).load();

                    return this;
                }

                this.files.put(strippedName, new CustomFile(fileType, file, isDynamic).load());
            }

            case JSON -> throw new FusionException("The file type with extension " + extension + " is not currently supported.");

            case NONE -> {} // do nothing
        }

        return this;
    }

    public FileManager saveFile(@NotNull final String fileName) {
        if (fileName.isBlank()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot save the file as the file is null or empty.");
            }

            return this;
        }

        final String extension = FileType.YAML.getExtension();

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

    public FileManager removeFile(final CustomFile customFile, final boolean purge) {
        return removeFile(customFile.getFileName(), customFile.getFileType(), purge);
    }

    public FileManager removeFile(@NotNull final String fileName, @NotNull final FileType fileType, final boolean purge) {
        if (fileName.isBlank()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot remove the file as the file is null or empty.");
            }

            return this;
        }

        final String strippedName = strip(fileName, fileType.getExtension());

        switch (fileType) {
            case YAML -> {
                if (!this.files.containsKey(strippedName)) return this;

                final CustomFile customFile = this.files.remove(fileName);

                if (purge) {
                    customFile.delete();

                    return this;
                }

                customFile.save();
            }

            case NBT -> {
                if (!this.files.containsKey(strippedName)) return this;

                final CustomFile customFile = this.files.remove(fileName);

                if (purge) {
                    customFile.delete();

                    return this;
                }
            }
        }

        return this;
    }

    public FileManager reloadFiles() {
        final List<String> forRemoval = new ArrayList<>();

        this.files.forEach((name, file) -> {
            if (file.getFile().exists()) {
                if (file.getFileType() == FileType.YAML) file.load();
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

    public FileManager init() {
        this.dataFolder.mkdirs();

        this.folders.forEach(this::addFolder);

        return this;
    }

    public FileManager purge() {
        this.folders.clear();
        this.files.clear();

        return this;
    }

    public @Nullable CustomFile getFile(final String fileName, final FileType fileType) {
        return this.files.getOrDefault(strip(fileName, fileType.getExtension()), null);
    }


    public String strip(final String fileName, final String extension) {
        return fileName.replace("." + extension, "");
    }

    public Map<String, CustomFile> getFiles() {
        return Collections.unmodifiableMap(this.files);
    }
}