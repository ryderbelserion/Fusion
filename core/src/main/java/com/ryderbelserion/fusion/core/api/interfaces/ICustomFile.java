package com.ryderbelserion.fusion.core.api.interfaces;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.files.FileType;
import com.ryderbelserion.fusion.core.files.FileAction;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class ICustomFile<A extends ICustomFile<A>> {

    protected final FusionCore fusion = FusionCore.Provider.get();
    protected final ILogger logger = this.fusion.getLogger();

    private final List<FileAction> actions;
    private final Path path;

    public ICustomFile(@NotNull final Path path, @NotNull final List<FileAction> actions) {
        this.actions = actions;
        this.path = path;
    }

    public abstract A load();

    public A save(final String content, @NotNull final List<FileAction> actions) {
        return save();
    }

    public A save() {
        return (A) this;
    }

    public A delete() {
        try {
            Files.deleteIfExists(getPath());

            this.logger.warn("Successfully deleted {}", getFileName());
        } catch (final IOException exception) {
            this.logger.warn("Failed to delete {}: {}", getPath(), exception);
        }

        return (A) this;
    }

    public boolean isDirectory() {
        return Files.isDirectory(this.path);
    }

    public String getFileName() {
        return this.path.getFileName().toString();
    }

    public Path getPath() {
        return this.path;
    }

    public boolean isLoaded() {
        return Files.exists(this.path);
    }

    public boolean isStatic() {
        return this.actions.contains(FileAction.STATIC);
    }

    public FileType getFileType() {
        return FileType.NONE;
    }
}