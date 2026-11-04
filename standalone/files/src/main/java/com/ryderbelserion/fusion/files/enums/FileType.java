package com.ryderbelserion.fusion.files.enums;

public enum FileType {

    YAML(".yml"),
    PNG(".png");

    private final String extension;

    FileType(final String extension) {
        this.extension = extension;
    }

    public final String getExtension() {
        return this.extension;
    }
}