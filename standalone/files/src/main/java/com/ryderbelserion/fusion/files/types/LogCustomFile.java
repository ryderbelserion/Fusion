package com.ryderbelserion.fusion.files.types;

import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.interfaces.ICustomFile;
import org.jspecify.annotations.NullMarked;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

@NullMarked
public final class LogCustomFile extends ICustomFile<LogCustomFile, LogCustomFile, Object> {

    public LogCustomFile(final FileManager fileManager, final Path path, final Consumer<LogCustomFile> consumer) {
        super(fileManager, path);

        consumer.accept(this);
    }

    @Override
    public LogCustomFile loadConfig() throws IOException {
        final Path path = getPath();

        if (Files.exists(path)) {
            return this;
        }

        Files.createFile(path);

        return this;
    }

    @Override
    public void saveConfig(final String content) {
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
    public FileType getFileType() {
        return FileType.LOG;
    }
}