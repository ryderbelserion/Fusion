package com.ryderbelserion.fusion.common.api.interfaces.files;

import com.ryderbelserion.fusion.common.api.FusionCommon;
import com.ryderbelserion.fusion.common.FusionProvider;
import com.ryderbelserion.fusion.common.api.enums.FileAction;
import com.ryderbelserion.fusion.common.api.enums.FileType;
import com.ryderbelserion.fusion.common.api.exceptions.FusionException;
import com.ryderbelserion.fusion.common.api.interfaces.ILogger;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Represents a customizable file object that allows for type-safe manipulation.
 *
 * @param <A> the type of the custom file instance, enabling self-referential generics
 */
public abstract class ICustomFile<A extends ICustomFile<A>> {

    protected final FusionCommon fusion = FusionProvider.get();
    protected final ILogger logger = this.fusion.getLogger();

    private final List<FileAction> actions;
    private final Path path;

    protected ICustomFile(@NotNull final Path path, @NotNull final List<FileAction> actions) {
        this.actions = actions;
        this.path = path;
    }

    /**
     * Loads the configuration, This is a generic way to support multiple configuration types.
     *
     * @return {@link A}
     */
    public abstract @NotNull A load();

    /**
     * Saves the configuration, usually used for log files.
     *
     * @param content the content to write to the file
     * @param actions list of actions
     * @return {@link A}
     */
    public @NotNull A save(@NotNull final String content, @NotNull final List<FileAction> actions) {
        return save();
    }

    /**
     * Saves the configuration as is.
     *
     * @return {@link A}
     */
    public @NotNull A save() {
        return (A) this;
    }

    /**
     * Deletes a file if it exists.
     *
     * @return {@link A}
     */
    public @NotNull A delete() {
        try {
            Files.deleteIfExists(getPath());

            this.logger.warn("Successfully deleted {}!", getFileName());
        } catch (final IOException exception) {
            throw new FusionException(String.format("Failed to delete %s!", getPath()), exception);
        }

        return (A) this;
    }

    /**
     * Checks if the relative path is a directory.
     *
     * @return true or false
     */
    public boolean isDirectory() {
        return Files.isDirectory(this.path);
    }

    /**
     * Retrieves the name of the relative path.
     *
     * @return the name
     */
    public @NotNull String getFileName() {
        return this.path.getFileName().toString();
    }

    /**
     * Retrieves the relative path.
     *
     * @return the path
     */
    public @NotNull Path getPath() {
        return this.path;
    }

    /**
     * Checks if the relative path exists.
     *
     * @return true or false
     */
    public boolean isLoaded() {
        return Files.exists(this.path);
    }

    /**
     * Checks if the instance of this file is static.
     *
     * @return true or false
     */
    public boolean isStatic() {
        return this.actions.contains(FileAction.STATIC_FILE);
    }

    /**
     * Retrieves the file type.
     *
     * @return the {@link FileType}
     */
    public @NotNull FileType getFileType() {
        return FileType.NONE;
    }
}