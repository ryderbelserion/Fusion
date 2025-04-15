package com.ryderbelserion.fusion.core.api.enums;

public enum FileType {

    YAML(".yml"),

    JALU(".jalu"),

    JSON(".json"),

    NONE("none"),

    NBT(".nbt"),

    LOG(".log");

    private final String extension;

    FileType(final String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return this.extension;
    }
}