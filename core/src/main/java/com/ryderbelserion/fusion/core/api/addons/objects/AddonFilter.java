package com.ryderbelserion.fusion.core.api.addons.objects;

import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.FilenameFilter;

/**
 * A custom filter to only accept files that end with .jar or .zip.
 *
 * @author ryderbelserion, prosavage
 */
public class AddonFilter implements FilenameFilter {

    /**
     * Tests if a specified file should be included in a file list.
     *
     * @param directory    the directory in which the file was found.
     * @param name         the name of the file.
     * @return             true or false
     */
    @Override
    public boolean accept(@NotNull final File directory, @NotNull final String name) {
        @NotNull final String lower = name.toLowerCase();

        return lower.endsWith(".jar") || lower.endsWith(".zip");
    }
}