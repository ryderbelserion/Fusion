package com.ryderbelserion.fusion.core.api.interfaces;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.files.FileType;
import com.ryderbelserion.fusion.core.files.FileAction;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

public abstract class ICustomFile<A extends ICustomFile<A>> {

    protected final FusionCore api = FusionCore.Provider.get();
    protected final Logger logger = this.api.getLogger();
    protected final boolean isVerbose = this.api.isVerbose();

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

            if (this.isVerbose) {
                this.logger.warning(String.format("Successfully deleted %s", getFileName()));
            }
        } catch (final IOException exception) {
            this.logger.warning(String.format("Failed to delete %s: %s", getPath(), exception));
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