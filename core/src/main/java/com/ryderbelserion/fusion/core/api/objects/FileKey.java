package com.ryderbelserion.fusion.core.api.objects;

import org.jetbrains.annotations.NotNull;

public class FileKey extends StringKey{

    private final String extension;

    public FileKey(@NotNull String extension, @NotNull String key) {
        super(key);

        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    @Override
    public @NotNull String toString() {
        return "FileKey{extension=" + this.extension + ", super=" + super.toString() + "}";
    }
}