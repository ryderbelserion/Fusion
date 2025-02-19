package com.ryderbelserion.fusion.core.files.v2;

import com.ryderbelserion.fusion.core.FusionLayout;
import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.api.enums.FileType;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import java.io.File;

public abstract class BaseCustomFile<T extends BaseCustomFile<T>> {

    protected final FusionLayout api = FusionProvider.get();

    protected final ComponentLogger logger = this.api.getLogger();

    protected final boolean isVerbose = this.api.isVerbose();

    private final File file;

    public BaseCustomFile(@NotNull final File file) {
        this.file = file;
    }

    public abstract BaseCustomFile<T> build();

    public abstract BaseCustomFile<T> load();

    public abstract BaseCustomFile<T> save();

    public BaseCustomFile<T> delete() {
        final File file = getFile();

        if (file != null && file.exists() && file.delete()) {
            if (this.isVerbose) {
                this.logger.warn("Successfully deleted {}", getFileName());
            }
        }

        return this;
    }

    public BaseCustomFile<T> getInstance() {
        return this;
    }

    public FileType getType() {
        return FileType.NONE;
    }

    public String getFileName() {
        return this.file.getName();
    }

    public File getFile() {
        return this.file;
    }
}