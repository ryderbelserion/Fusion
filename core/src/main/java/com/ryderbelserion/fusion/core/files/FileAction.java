package com.ryderbelserion.fusion.core.files;

/**
 * File actions responsible for telling FileManager what to do.
 */
public enum FileAction {

    /**
     * Deletes a file if specified.
     */
    DELETE,
    /**
     * Tells the FileManager or extraction utils that this is a folder.
     */
    FOLDER,
    /**
     * Tells the FileManager that we are reloading, and have a niche use case.
     */
    RELOAD,
    /**
     * Tells the FileManager we need to extract!
     */
    EXTRACT,
    /**
     * Tells the FileManager that the file is not dynamic.
     */
    STATIC
}