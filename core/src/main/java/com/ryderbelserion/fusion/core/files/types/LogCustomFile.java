package com.ryderbelserion.fusion.core.files.types;

import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.enums.FileType;
import com.ryderbelserion.fusion.core.files.interfaces.ICustomFile;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public final class LogCustomFile extends ICustomFile<LogCustomFile, LogCustomFile, Object, Object> {

    public LogCustomFile(@NotNull final FileManager fileManager, @NotNull final Path path, @NotNull final Consumer<LogCustomFile> consumer) {
        super(fileManager, path);

        consumer.accept(this);
    }

    @Override
    public @NotNull LogCustomFile loadConfig() throws IOException {
        Files.createFile(getPath());

        return this;
    }

    @Override
    public void saveConfig(@NotNull final String content) {
        if (content.isEmpty()) {
            saveConfig();

            return;
        }

        this.fileManager.compressFile(getPath(), content);
    }

    @Override
    public void saveConfig() {
        this.fileManager.compressFile(getPath());
    }

    @Override
    public @NotNull FileType getFileType() {
        return FileType.LOG;
    }
}