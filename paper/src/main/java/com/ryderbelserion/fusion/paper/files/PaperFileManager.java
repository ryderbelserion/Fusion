package com.ryderbelserion.fusion.paper.files;

import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.files.types.PaperCustomFile;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;

public class PaperFileManager extends FileManager {

    public PaperFileManager(@NotNull final FusionPaper fusion) {
        super(fusion);
    }

    public final PaperFileManager addPaperFile(@NotNull final Path path, @NotNull final Consumer<PaperCustomFile> consumer) {
        addFile(path, new PaperCustomFile(this, path, consumer).load());

        return this;
    }

    public final @NotNull Optional<PaperCustomFile> getPaperFile(@NotNull final Path path) {
        return getFile(path).map(PaperCustomFile.class::cast);
    }
}