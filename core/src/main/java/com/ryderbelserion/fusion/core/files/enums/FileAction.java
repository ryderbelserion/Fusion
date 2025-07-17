package com.ryderbelserion.fusion.core.files.enums;

public enum FileAction {

    // Extract a folder.
    EXTRACT_FOLDER,
    // Extract a file.
    EXTRACT_FILE,

    // Delete the folder, and  remove all files inside from the cache.
    DELETE_FOLDER,
    // Delete from cache.
    DELETE_CACHE,
    // Delete the file and remove from cache.
    DELETE_FILE,

    // The files with this tag are user created.
    DYNAMIC_FILE,

    // Do not extract at all.
    MANUALLY_SAVED

}