package com.ryderbelserion.fusion.paper.files;

import com.ryderbelserion.fusion.core.api.support.objects.FusionKey;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.files.types.PaperCustomFile;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

public class PaperFileManager extends FileManager {

    public PaperFileManager(@NotNull final FusionPaper fusion) {
        super(fusion);
    }

    public final PaperFileManager addPaperFile(@NotNull final FusionKey key, @NotNull final Consumer<PaperCustomFile> consumer) {
        addFile(key, new PaperCustomFile(this, consumer).load());

        return this;
    }

    public final PaperCustomFile getPaperFile(@NotNull final FusionKey key) {
        return (PaperCustomFile) getFile(key);
    }
}