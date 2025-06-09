package com.ryderbelserion.fusion.core.files;

/**
 * File actions responsible for telling FileManager what to do.
 */
public enum FileAction {

    /**
     * Tells the FileManager or extraction utils that this is a folder to be extracted.
     */
    EXTRACT_FOLDER,
    /**
     * Tells the FileManager or extraction utils that this is a folder where I specify a file to be extracted.
     * i.e. path.resolve("cache").resolve("ores.json")
     */
    EXTRACT_FILE,
    //EXTRACT,
    /**
     * Tells the FileManager that we are reloading, and have a niche use case.
     */
    RELOAD,
    /**
     * Deletes a file if specified.
     */
    DELETE,
    /**
     * Tells the FileManager that the file is not dynamic.
     */
    STATIC
}