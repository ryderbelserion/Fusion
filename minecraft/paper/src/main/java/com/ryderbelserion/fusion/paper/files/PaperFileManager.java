package com.ryderbelserion.fusion.paper.files;

import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.paper.files.types.PaperCustomFile;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;

public class PaperFileManager extends FileManager {

    public PaperFileManager(@NotNull final Path path) {
        super(path);
    }

    public final PaperFileManager addPaperFile(@NotNull final Path path, @NotNull final Consumer<PaperCustomFile> consumer) {
        if (this.files.containsKey(path)) {
            final PaperCustomFile customFile = (PaperCustomFile) this.files.get(path);

            consumer.accept(customFile);

            if (customFile.hasAction(FileAction.RELOAD_FILE)) {
                customFile.load();
            }

            return this;
        }

        addFile(path, new PaperCustomFile(this, path, consumer).load());

        return this;
    }

    public PaperFileManager addPaperFolder(@NotNull final Path folder, @NotNull final Consumer<PaperCustomFile> consumer) {
        extractFolder(folder.getFileName().toString(), folder.getParent());

        for (final Path path : getFilesByPath(folder, ".yml", getDepth())) {
            addPaperFile(path, consumer);
        }

        return this;
    }

    public PaperCustomFile buildPaperFile(@NotNull final Path path, @NotNull final Consumer<PaperCustomFile> consumer) {
        return new PaperCustomFile(this, path, consumer);
    }

    public PaperFileManager savePaperFile(@NotNull final PaperCustomFile customFile) {
        customFile.save();

        return this;
    }

    public PaperFileManager addPaperFile(@NotNull final PaperCustomFile customFile) {
        addFile(customFile.getPath(), customFile);

        return this;
    }

    public @NotNull Optional<PaperCustomFile> getPaperFile(@NotNull final Path path) {
        return getFile(path).map(PaperCustomFile.class::cast);
    }

    public final PaperFileManager addPaperFile(@NotNull final Path path) {
        return addPaperFile(path, consumer -> consumer.addAction(FileAction.EXTRACT_FILE));
    }

    public PaperFileManager addPaperFolder(@NotNull final Path folder) {
        return addPaperFolder(folder, consumer -> consumer.addAction(FileAction.EXTRACT_FOLDER));
    }
}