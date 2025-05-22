package com.ryderbelserion.fusion.core.files.types;

import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.files.FileType;
import com.ryderbelserion.fusion.core.api.interfaces.ICustomFile;
import com.ryderbelserion.fusion.core.files.FileAction;
import com.ryderbelserion.fusion.core.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LogCustomFile extends ICustomFile<LogCustomFile> {

    public LogCustomFile(@NotNull final Path path, @NotNull final List<FileAction> actions) {
        super(path, actions);
    }

    @Override
    public LogCustomFile load() {
        final Path path = getPath();

        if (Files.exists(path)) return this;

        try {
            Files.createFile(path);
        } catch (final IOException exception) {
            throw new FusionException("Could not create file " + getFileName(), exception);
        }

        return this;
    }

    @Override
    public LogCustomFile save(@NotNull final String content, @NotNull final List<FileAction> actions) {
        final Path path = getPath();

        if (actions.contains(FileAction.RELOAD)) {
            try {
                FileUtils.compress(path, null, "", actions);
            } catch (final IOException exception) {
                throw new FusionException("Could not compress file " + getFileName(), exception);
            }
        }

        load();

        FileUtils.write(path.toFile(), content);

        return this;
    }

    @Override
    public final FileType getFileType() {
        return FileType.LOG;
    }
}