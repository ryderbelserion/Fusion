package com.ryderbelserion.api.enums;

public enum FileType {

    YAML("yml"),

    JSON("json"),

    NONE("none");

    private final String extension;

    FileType(final String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return this.extension;
    }
}