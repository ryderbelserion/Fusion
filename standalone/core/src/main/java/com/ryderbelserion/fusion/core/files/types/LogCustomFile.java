package com.ryderbelserion.fusion.core.files.types;

import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.enums.FileType;
import com.ryderbelserion.fusion.core.files.interfaces.ICustomFile;
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
        if (Files.exists(this.path)) {
            return this;
        }

        Files.createFile(this.path);

        return this;
    }

    @Override
    public void saveConfig(final String content) {
        if (content.isEmpty()) {
            saveConfig();

            return;
        }

        this.fileManager.compressFile(this.path, content);
    }

    @Override
    public void saveConfig() {
        this.fileManager.compressFile(this.path);
    }

    @Override
    public FileType getFileType() {
        return FileType.LOG;
    }
}