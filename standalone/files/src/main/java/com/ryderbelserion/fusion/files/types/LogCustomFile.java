package com.ryderbelserion.fusion.files.types;

import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.interfaces.ICustomFile;
import org.jspecify.annotations.NonNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public final class LogCustomFile extends ICustomFile<LogCustomFile, LogCustomFile, Object> {

    public LogCustomFile(@NonNull final FileManager fileManager, @NonNull final Path path, @NonNull final Consumer<LogCustomFile> consumer) {
        super(fileManager, path);

        consumer.accept(this);
    }

    @Override
    public @NonNull LogCustomFile loadConfig() throws IOException {
        final Path path = getPath();

        if (Files.exists(path)) {
            return this;
        }

        Files.createFile(path);

        return this;
    }

    @Override
    public void saveConfig(@NonNull final String content) {
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
    public @NonNull FileType getFileType() {
        return FileType.LOG;
    }
}