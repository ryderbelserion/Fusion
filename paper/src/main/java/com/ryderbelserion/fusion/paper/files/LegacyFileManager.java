package com.ryderbelserion.fusion.paper.files;

import com.ryderbelserion.fusion.core.api.FusionCore;
import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.api.enums.FileType;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.enums.FileAction;
import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
import com.ryderbelserion.fusion.core.api.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LegacyFileManager {

    private final FusionCore api = FusionProvider.get();

    private final ILogger logger = this.api.getLogger();

    private final File dataFolder = this.api.getPath().toFile();

    private final Map<String, LegacyCustomFile> files = new HashMap<>();

    private final Map<String, FileType> folders = new HashMap<>();

    public LegacyFileManager() {}

    public @NotNull final LegacyFileManager addFolder(@NotNull final String folder, @NotNull final FileType fileType) {
        if (folder.isBlank()) {
            this.logger.warn("Cannot add the folder as the folder is empty.");

            return this;
        }

        if (!this.folders.containsKey(folder)) {
            this.folders.put(folder, fileType);
        }

        final Path dataPath = this.dataFolder.toPath();

        extractFolder(dataPath.resolve(folder), new ArrayList<>());

        final List<Path> files = FileUtils.getFiles(dataPath.resolve(folder), ".yml", 2);

        final String extension = fileType.getExtension();

        for (final Path path : files) {
            final String fileName = path.getFileName().toString();

            this.logger.warn("Adding file: " + folder + File.separator + fileName);

            if (!fileName.endsWith(extension)) continue;

            addFile(fileName, folder + File.separator + fileName, true, fileType);
        }

        return this;
    }

    public @NotNull final LegacyFileManager addFile(@NotNull final LegacyCustomFile customFile) {
        this.files.put(customFile.getEffectiveName(), customFile);
        
        return this;
    }

    public @NotNull final LegacyFileManager addFile(@NotNull final String fileName) {
        FileType type = FileType.NONE;

        if (fileName.endsWith(".yml")) {
            type = FileType.YAML;
        } else if (fileName.endsWith(".nbt")) {
            type = FileType.NBT;
        }

        return addFile(fileName, null, false, type);
    }

    public @NotNull final LegacyFileManager addFile(@NotNull final String fileName, @NotNull final FileType fileType) {
        return addFile(fileName, null, false, fileType);
    }

    public @NotNull final LegacyFileManager addFile(@NotNull final String fileName, @Nullable final String folder, final boolean isDynamic, @NotNull final FileType fileType) {
        if (fileName.isBlank()) {
            this.logger.warn("Cannot add the file as the file is null or empty.");

            return this;
        }

        final String extension = fileType.getExtension();

        final String strippedName = strip(fileName, extension);

        final String resourcePath = folder != null ? folder + File.separator + fileName : fileName;

        final Path file = this.dataFolder.toPath().resolve(resourcePath);

        if (!Files.exists(file)) {
            this.logger.warn("Successfully extracted file {} to {}", fileName, file);

            FileUtils.extract(file.getFileName().toString(), file.getParent(), new ArrayList<>());
        }

        switch (fileType) {
            case NBT -> {
                if (this.files.containsKey(strippedName)) {
                    throw new FusionException(String.format("The file '%s' already exists.", strippedName));
                }

                this.files.put(strippedName, new LegacyCustomFile(fileType, file.toFile(), isDynamic));
            }

            case YAML -> {
                if (this.files.containsKey(strippedName)) {
                    this.files.get(strippedName).load();

                    return this;
                }

                this.files.put(strippedName, new LegacyCustomFile(fileType, file.toFile(), isDynamic).load());
            }

            //case JSON -> throw new FusionException("The file type with extension " + extension + " is not currently supported.");

            case NONE -> {} // do nothing
        }

        return this;
    }

    public @NotNull final LegacyFileManager saveFile(@NotNull final String fileName) {
        if (fileName.isBlank()) {
            this.logger.warn("Cannot save the file as the file is null or empty.");

            return this;
        }

        final String extension = FileType.YAML.getExtension();

        final String strippedName = strip(fileName, extension);

        if (!this.files.containsKey(strippedName)) {
            this.logger.warn("Cannot save the file as the file does not exist.");

            return this;
        }

        this.files.get(strippedName).save();

        return this;
    }

    public @NotNull final LegacyFileManager removeFile(@NotNull final LegacyCustomFile customFile, final boolean purge) {
        return removeFile(customFile.getFileName(), customFile.getFileType(), purge);
    }

    public @NotNull final LegacyFileManager removeFile(@NotNull final String fileName, @NotNull final FileType fileType, final boolean purge) {
        if (fileName.isBlank()) {
            this.logger.warn("Cannot remove the file as the file is null or empty.");

            return this;
        }

        final String strippedName = strip(fileName, fileType.getExtension());

        switch (fileType) {
            case YAML -> {
                if (!this.files.containsKey(strippedName)) return this;

                final LegacyCustomFile customFile = this.files.remove(fileName);

                if (purge) {
                    customFile.delete();

                    return this;
                }

                customFile.save();
            }

            case NBT -> {
                if (!this.files.containsKey(strippedName)) return this;

                final LegacyCustomFile customFile = this.files.remove(fileName);

                if (purge) {
                    customFile.delete();

                    return this;
                }
            }
        }

        return this;
    }

    public @NotNull final LegacyFileManager reloadFiles() {
        final List<String> forRemoval = new ArrayList<>();

        this.files.forEach((name, file) -> {
            if (file.getFile().exists()) {
                if (file.getFileType() == FileType.YAML) file.load();
            } else {
                forRemoval.add(name);
            }
        });

        forRemoval.forEach(this.files::remove);

        if (!forRemoval.isEmpty()) {
            this.logger.safe("{} file(s) were removed from cache, because they did not exist.", forRemoval.size());
        }

        return this;
    }

    public @NotNull final LegacyFileManager init() {
        this.dataFolder.mkdirs();

        this.folders.forEach(this::addFolder);

        return this;
    }

    public @NotNull final LegacyFileManager purge() {
        this.folders.clear();
        this.files.clear();

        return this;
    }

    public @Nullable final LegacyCustomFile getFile(@NotNull final String fileName, @NotNull final FileType fileType) {
        return this.files.getOrDefault(strip(fileName, fileType.getExtension()), null);
    }

    public @NotNull final String strip(@NotNull final String fileName, @NotNull final String extension) {
        return fileName.replace(extension, "");
    }

    public @NotNull final Map<String, LegacyCustomFile> getFiles() {
        return Collections.unmodifiableMap(this.files);
    }

    /**
     * Extracts a folder with a specified list of actions.
     *
     * @param folder the folder to extract
     * @param actions the list of actions to define what to do.
     * @return {@link LegacyFileManager}
     */
    public @NotNull final LegacyFileManager extractFolder(@NotNull final Path folder, @NotNull final List<FileAction> actions) {
        actions.add(FileAction.EXTRACT_FOLDER);

        FileUtils.extract(folder.getFileName().toString(), this.dataFolder.toPath(), actions);

        return this;
    }
}