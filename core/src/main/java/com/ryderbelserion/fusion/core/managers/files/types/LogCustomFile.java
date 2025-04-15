package com.ryderbelserion.fusion.core.managers.files.types;

import com.ryderbelserion.fusion.core.api.enums.FileType;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.interfaces.ICustomFile;
import com.ryderbelserion.fusion.core.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LogCustomFile extends ICustomFile<LogCustomFile> {

    public LogCustomFile(@NotNull final Path path, final boolean isStatic) {
        super(path, isStatic);
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
        try {
            FileUtils.compress(getPath(), null, "", true);
        } catch (final IOException exception) {
            throw new FusionException("Could not compress file " + getFileName(), exception);
        }

        build();

        return this;
    }

    @Override
    public final LogCustomFile write(@NotNull final String content) {
        FileUtils.write(getPath().toFile(), content);

        return this;
    }

    @Override
    public final FileType getType() {
        return FileType.LOG;
    }
}