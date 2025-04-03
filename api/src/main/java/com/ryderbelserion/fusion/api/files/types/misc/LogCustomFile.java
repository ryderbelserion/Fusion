package com.ryderbelserion.fusion.api.files.types.misc;

import com.ryderbelserion.fusion.api.enums.FileType;
import com.ryderbelserion.fusion.api.exceptions.FusionException;
import com.ryderbelserion.fusion.api.files.CustomFile;
import com.ryderbelserion.fusion.api.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LogCustomFile extends CustomFile<LogCustomFile> {

    public LogCustomFile(@NotNull final Path path, final boolean isDynamic) {
        super(path, isDynamic);
    }

    @Override
    public final LogCustomFile build() {
        try {
            Files.createFile(getPath());
        } catch (final IOException exception) {
            throw new FusionException("Could not create file " + getFileName(), exception);
        }

        return this;
    }

    @Override
    public final LogCustomFile load() {
        return save();
    }

    @Override
    public final LogCustomFile save() {
        build();

        return this;
    }

    @Override
    public final LogCustomFile saveDirectory() {
        return this;
    }

    @Override
    public CustomFile<LogCustomFile> write(@NotNull final String content) {
        FileUtils.write(getPath().toFile(), content);

        return this;
    }

    @Override
    public final FileType getType() {
        return FileType.LOG;
    }
}