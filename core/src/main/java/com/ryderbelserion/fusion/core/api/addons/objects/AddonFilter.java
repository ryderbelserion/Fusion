package com.ryderbelserion.fusion.core.api.addons.objects;

import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.FilenameFilter;

public class AddonFilter implements FilenameFilter {

    @Override
    public boolean accept(@NotNull final File directory, @NotNull final String name) {
        String lower = name.toLowerCase();

        return lower.endsWith(".jar") || lower.endsWith(".zip");
    }
}