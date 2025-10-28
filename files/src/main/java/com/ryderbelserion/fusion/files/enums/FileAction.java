package com.ryderbelserion.fusion.files.enums;

public enum FileAction {

    // Extract a file to a specific path. usually done manually.
    ALREADY_EXTRACTED,
    // Extract a folder.
    EXTRACT_FOLDER,
    // Extract a file.
    EXTRACT_FILE,

    // Delete the folder, and remove all files inside from the cache.
    DELETE_FOLDER,
    // Delete from cache.
    DELETE_CACHE,
    // Delete the file and remove from cache.
    DELETE_FILE,

    // Do not remove file from cache.
    KEEP_FILE,

}