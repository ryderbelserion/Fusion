package com.ryderbelserion.fusion.paper.files;

import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.paper.files.types.PaperCustomFile;
import org.jspecify.annotations.NonNull;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;

public class PaperFileManager extends FileManager {

    public PaperFileManager(@NonNull final Path path) {
        super(path);
    }

    public @NonNull final PaperFileManager addPaperFile(@NonNull final Path path, @NonNull final Consumer<PaperCustomFile> consumer) {
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

    public @NonNull final PaperFileManager addPaperFile(@NonNull final PaperCustomFile customFile) {
        addFile(customFile.getPath(), customFile);

        return this;
    }

    public @NonNull final PaperFileManager addPaperFile(@NonNull final Path path) {
        return addPaperFile(path, consumer -> consumer.addAction(FileAction.EXTRACT_FILE));
    }

    public @NonNull final PaperFileManager addPaperFolder(@NonNull final Path folder, @NonNull final Consumer<PaperCustomFile> consumer) {
        extractFolder(folder.getFileName().toString(), folder.getParent());

        for (final Path path : getFilesByPath(folder, ".yml", getDepth())) {
            addPaperFile(path, consumer);
        }

        return this;
    }

    public @NonNull final PaperFileManager addPaperFolder(@NonNull final Path folder) {
        return addPaperFolder(folder, consumer -> consumer.addAction(FileAction.EXTRACT_FOLDER));
    }

    public @NonNull final PaperCustomFile buildPaperFile(@NonNull final Path path, @NonNull final Consumer<PaperCustomFile> consumer) {
        return new PaperCustomFile(this, path, consumer);
    }

    public @NonNull final PaperFileManager savePaperFile(@NonNull final PaperCustomFile customFile) {
        customFile.save();

        return this;
    }

    public @NonNull final Optional<PaperCustomFile> getPaperFile(@NonNull final Path path) {
        return getFile(path).map(PaperCustomFile.class::cast);
    }
}