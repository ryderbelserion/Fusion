package com.ryderbelserion.fusion.core.files.types;

import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.enums.FileType;
import com.ryderbelserion.fusion.core.files.interfaces.ICustomFile;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Consumer;

public class LogCustomFile extends ICustomFile<LogCustomFile, LogCustomFile, Object, Object> {

    public LogCustomFile(@NotNull final FileManager fileManager, @NotNull final Consumer<LogCustomFile> consumer) {
        super(fileManager);

        consumer.accept(this);
    }

    @Override
    public @NotNull final LogCustomFile loadConfig() throws IOException {
        Files.createFile(getPath());

        return this;
    }

    @Override
    public final void saveConfig(@NotNull final String content) {
        if (content.isEmpty()) {
            saveConfig();

            return;
        }

        this.fileManager.compressFile(getPath(), content);
    }

    @Override
    public final void saveConfig() {
        this.fileManager.compressFile(getPath());
    }

    @Override
    public @NotNull final FileType getFileType() {
        return FileType.LOG;
    }
}