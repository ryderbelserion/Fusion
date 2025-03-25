package com.ryderbelserion.fusion.api.files.types.misc;

import com.ryderbelserion.fusion.api.enums.FileType;
import com.ryderbelserion.fusion.api.exceptions.FusionException;
import com.ryderbelserion.fusion.api.files.CustomFile;
import com.ryderbelserion.fusion.api.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;

public class LogCustomFile extends CustomFile<LogCustomFile> {

    private final File directory;

    public LogCustomFile(@NotNull final File file, @NotNull final File directory, final boolean isDynamic) {
        super(file, isDynamic);

        this.directory = directory;
    }

    @Override
    public final LogCustomFile build() {
        if (!getFile().exists()) {
            try {
                getFile().createNewFile();
            } catch (final IOException exception) {
                throw new FusionException("Could not create file " + getFile(), exception);
            }
        }

        return this;
    }

    @Override
    public final LogCustomFile load() {
        return save();
    }

    @Override
    public final LogCustomFile save() { // this will zip the folder
        if (this.directory.exists()) { // only zip if directory exists
            FileUtils.zip(this.directory, getType().getExtension(), true); // zip folder
        }

        build(); // create new file

        return this;
    }

    @Override
    public CustomFile<LogCustomFile> write(@NotNull final String content) {
        super.write(content);

        FileUtils.write(getFile(), content);

        return this;
    }

    @Override
    public final FileType getType() {
        return FileType.LOG;
    }
}