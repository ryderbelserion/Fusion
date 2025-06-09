package com.ryderbelserion.fusion.core.files;

import org.jetbrains.annotations.NotNull;

/**
 * Holds the FileType's which are used in {@link FileManager} or {@link com.ryderbelserion.fusion.core.utils.FileUtils}
 */
public enum FileType {

    /**
     * Files with the extension .yml
     */
    YAML(".yml"),
    /**
     * Files with the extension .json
     */
    JSON(".json"),
    /**
     * Files with no extension.
     */
    NONE("none"),
    /**
     * Files with the extension .nbt
     */
    NBT(".nbt"),
    /**
     * Files with the extension .log
     */
    LOG(".log");

    private final String extension;

    /**
     * Creates a file type
     *
     * @param extension the file extension
     */
    FileType(@NotNull final String extension) {
        this.extension = extension;
    }

    /**
     * Gets the file type's extension.
     *
     * @return the file type extension
     */
    public @NotNull final String getExtension() {
        return this.extension;
    }
}