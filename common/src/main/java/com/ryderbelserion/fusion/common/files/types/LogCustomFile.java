package com.ryderbelserion.fusion.common.files.types;

import com.ryderbelserion.fusion.common.api.exceptions.FusionException;
import com.ryderbelserion.fusion.common.api.utils.FileUtils;
import com.ryderbelserion.fusion.common.api.enums.FileType;
import com.ryderbelserion.fusion.common.api.interfaces.files.ICustomFile;
import com.ryderbelserion.fusion.common.api.enums.FileAction;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LogCustomFile extends ICustomFile<LogCustomFile> {

    /**
     * Constructs a {@code LogCustomFile} with the specified file path, actions.
     *
     * @param path    the file path associated with the configuration file
     * @param actions the list of file actions applied to the configuration file
     */
    public LogCustomFile(@NotNull final Path path, @NotNull final List<FileAction> actions) {
        super(path, actions);
    }

    /**
     * Creates the log file.
     *
     * @return {@link LogCustomFile}
     */
    @Override
    public @NotNull final LogCustomFile load() {
        final Path path = getPath();

        if (Files.exists(path)) return this;

        try {
            Files.createFile(path);
        } catch (final IOException exception) {
            throw new FusionException(String.format("Could not create file %s!", getFileName()), exception);
        }

        return this;
    }

    /**
     * Saves the content to the log file.
     *
     * @param content the content to write to the file
     * @param actions list of actions
     * @return {@link LogCustomFile}
     */
    @Override
    public @NotNull final LogCustomFile save(@NotNull final String content, @NotNull final List<FileAction> actions) {
        final Path path = getPath();

        if (actions.contains(FileAction.RELOAD_FILE)) {
            try {
                FileUtils.compress(path, null, "", actions);
            } catch (final IOException exception) {
                throw new FusionException(String.format("Could not compress file %s!", getFileName()), exception);
            }
        }

        load();

        FileUtils.write(path.toFile(), content);

        return this;
    }

    /**
     * Retrieves the file type.
     *
     * @return the {@link FileType}
     */
    @Override
    public @NotNull final FileType getFileType() {
        return FileType.LOG;
    }
}