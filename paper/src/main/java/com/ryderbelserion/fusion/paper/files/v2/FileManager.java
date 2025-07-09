package com.ryderbelserion.fusion.paper.files.v2;

import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.api.FusionCore;
import com.ryderbelserion.fusion.core.api.enums.FileAction;
import com.ryderbelserion.fusion.core.api.enums.FileType;
import com.ryderbelserion.fusion.core.api.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileManager {

    private final FusionCore fusion = FusionProvider.get();
    private final Path path = this.fusion.getPath();

    private final Map<Path, CustomFile> files = new HashMap<>();
    private final Map<Path, FileType> folders = new HashMap<>();

    private static final Map<String, FileType> fileMap = Map.of(
            ".yml", FileType.YAML,
            ".nbt", FileType.NBT
    );

    public @NotNull final FileManager addFolder(@NotNull final Path path, @NotNull final FileType fileType, @NotNull final List<FileAction> actions) {
        this.folders.put(path, fileType);

        extractFolder(path, actions);

        final List<Path> paths = FileUtils.getFiles(path, fileType.getExtension(), this.fusion.getDepth());

        for (final Path value : paths) {
            addFile(value, actions);
        }

        return this;
    }

    public @NotNull final FileManager addFile(@NotNull final Path path, @NotNull final List<FileAction> actions) {
        final String fileName = path.getFileName().toString();

        if (!Files.exists(path)) { // always extract if it does not exist.
            FileUtils.extract(fileName, path.getParent(), actions);
        }

        CustomFile file = this.files.getOrDefault(path, null);

        if (file != null && !actions.contains(FileAction.RELOAD_FILE)) { // if the reload action is not present, we load the file!
            file.load();

            return this;
        }

        final FileType fileType = detectFileType(fileName);

        switch (fileType) {
            case YAML -> {
                if (this.files.containsKey(path)) {
                    this.files.get(path).load();

                    return this;
                }

                file = new CustomFile(path, actions).load();
            }

            case NBT -> file = new CustomFile(path, actions);
        }

        if (file != null) {
            this.files.putIfAbsent(path, file);
        }

        return this;
    }

    public @NotNull final FileManager saveFile(@NotNull final Path path, @NotNull final List<FileAction> actions) {
        final CustomFile file = this.files.getOrDefault(path, null);

        if (file == null) {
            this.fusion.log("warn", "Could not find {}!", path);

            return this;
        }

        file.save();

        return this;
    }

    public @NotNull final FileManager removeFile(@NotNull final Path path, @Nullable final FileAction action) {
        if (!this.files.containsKey(path)) {
            this.fusion.log("warn", "Could not find {}!", path);

            return this;
        }

        final CustomFile file  = this.files.remove(path);

        file.save(); // save just in case.

        if (action == FileAction.DELETE_FILE) {
            file.delete();
        }

        return this;
    }

    public @NotNull final FileType detectFileType(@NotNull final String fileName) {
        return fileMap.entrySet().stream().filter(entry -> fileName.endsWith(entry.getKey())).map(Map.Entry::getValue).findFirst().orElse(FileType.NONE);
    }

    public @NotNull final FileManager extractFolder(@NotNull final Path path, @NotNull final List<FileAction> actions) {
        actions.add(FileAction.EXTRACT_FOLDER);

        FileUtils.extract(path.getFileName().toString(), this.path, actions);

        return this;
    }
}