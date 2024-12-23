package com.ryderbelserion.core.api.enums;

public enum FileType {

    YAML("yml"),

    JSON("json"),

    NONE("none"),

    NBT("nbt");

    private final String extension;

    FileType(final String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return this.extension;
    }
}