package com.ryderbelserion.fusion.core.files.enums;

import org.jetbrains.annotations.NotNull;

/**
 * Holds the FileType's used in FileUtils/FileManager.
 */
public enum FileType {

    /**
     * Files that end with .yml, but are built with Configurate which is by SpongePowered
     */
    CONFIGURATE_YAML(".yml"),
    /**
     * Files that end with .json, but are built with Configurate which is by SpongePowered
     */
    CONFIGURATE_GSON(".json"),
    /**
     * Files that end with .yml, but are built with Configurate which is by SpongePowered, and for internal use only.
     */
    FUSION_YAML(".yml"),
    /**
     * Files that end with .json, but are built with Configurate which is by SpongePowered, and for internal use only.
     */
    FUSION_GSON(".json"),
    /**
     * Files that end with .yml, but are built using YamlConfiguration supplied by Paper
     */
    PAPER(".yml"),
    /**
     * Files that end with .yml, but are made by ConfigMe
     */
    JALU(".yml"),
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
     * Creates a file type.
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