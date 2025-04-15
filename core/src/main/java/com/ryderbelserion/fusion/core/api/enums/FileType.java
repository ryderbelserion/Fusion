package com.ryderbelserion.fusion.core.api.enums;

import org.jetbrains.annotations.NotNull;

public enum FileType {

    YAML(".yml"),

    JALU(".jalu"),

    //JSON(".json"),

    NONE("none"),

    NBT(".nbt"),

    LOG(".log");

    private final String extension;

    FileType(@NotNull final String extension) {
        this.extension = extension;
    }

    public @NotNull final String getExtension() {
        return this.extension;
    }
}