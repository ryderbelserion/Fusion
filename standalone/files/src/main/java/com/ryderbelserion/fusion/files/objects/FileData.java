package com.ryderbelserion.fusion.files.objects;

import com.ryderbelserion.fusion.files.enums.FileType;
import java.nio.file.Path;

public record FileData(Path path, String jarPath, FileType fileType) {

    public FileData(final Path path, final FileType fileType) {
        this(path, "", fileType);
    }

    public String getPrettyName() {
        return getFileName().replace(fileType.getExtension(), "");
    }

    public String getFileName() {
        return this.path.getFileName().toString();
    }
}