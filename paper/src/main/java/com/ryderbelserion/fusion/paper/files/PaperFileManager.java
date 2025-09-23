package com.ryderbelserion.fusion.paper.files;

import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.enums.FileAction;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.files.types.NbtCustomFile;
import com.ryderbelserion.fusion.paper.files.types.PaperCustomFile;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;

public class PaperFileManager extends FileManager {

    private final FusionPaper fusion;

    public PaperFileManager(@NotNull final FusionPaper fusion) {
        super(fusion);

        this.fusion = fusion;
    }

    public final PaperFileManager addNbtFile(@NotNull final Path path) {
        if (this.files.containsKey(path)) {
            return this;
        }

        addFile(path, new NbtCustomFile(this.fusion.getPlugin(), this, path));

        return this;
    }

    public final PaperFileManager addNbtFolder(@NotNull final Path folder) {
        extractFolder(folder.getFileName().toString(), folder.getParent());

        for (final Path path : this.fusion.getFiles(folder, ".nbt", this.fusion.getDepth())) {
            addNbtFile(path);
        }

        return this;
    }

    public final Optional<NbtCustomFile> getNbtFile(@NotNull final Path path) {
        return getFile(path).map(NbtCustomFile.class::cast);
    }

    public final PaperFileManager addPaperFile(@NotNull final Path path, @NotNull final Consumer<PaperCustomFile> consumer) {
        if (this.files.containsKey(path)) {
            this.files.get(path).load();

            return this;
        }

        addFile(path, new PaperCustomFile(this, path, consumer).load());

        return this;
    }

    public final PaperFileManager addPaperFile(@NotNull final Path path) {
        return addPaperFile(path, consumer -> {});
    }

    public PaperFileManager addPaperFile(@NotNull final PaperCustomFile customFile) {
        addFile(customFile.getPath(), customFile);

        return this;
    }

    public PaperFileManager savePaperFile(@NotNull final PaperCustomFile customFile) {
        customFile.save();

        return this;
    }

    public PaperFileManager addPaperFolder(@NotNull final Path folder, @NotNull final Consumer<PaperCustomFile> consumer) {
        extractFolder(folder.getFileName().toString(), folder.getParent());

        for (final Path path : this.fusion.getFiles(folder, ".yml", this.fusion.getDepth())) {
            addPaperFile(path, consumer);
        }

        return this;
    }

    public PaperFileManager addPaperFolder(@NotNull final Path folder) {
        return addPaperFolder(folder, consumer -> {
            consumer.addAction(FileAction.EXTRACT_FOLDER);
        });
    }

    public @NotNull Optional<PaperCustomFile> getPaperFile(@NotNull final Path path) {
        return getFile(path).map(PaperCustomFile.class::cast);
    }
}