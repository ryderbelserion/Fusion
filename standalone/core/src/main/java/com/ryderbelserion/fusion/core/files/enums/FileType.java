package com.ryderbelserion.fusion.core.files.enums;

import org.jspecify.annotations.NullMarked;

/**
 * Holds the FileType's used in FileUtils/FileManager.
 */
@NullMarked
public enum FileType {

    /**
     * Files that end with .yml, but are built with Configurate which is by SpongePowered
     */
    YAML(".yml"),
    /**
     * Files that end with .json, but are built with Configurate which is by SpongePowered
     */
    JSON(".json"),
    /**
     * Files that end with .nbt, used for structure files
     */
    NBT(".nbt"),
    /**
     * Files with the extension .log
     */
    LOG(".log"),
    /**
     * Files with the extension .png
     */
    PNG(".png");

    private final String extension;

    /**
     * Creates a file type.
     *
     * @param extension the file extension
     */
    FileType(final String extension) {
        this.extension = extension;
    }

    /**
     * Gets the file type's extension.
     *
     * @return the file type extension
     */
    public final String getExtension() {
        return this.extension;
    }
}