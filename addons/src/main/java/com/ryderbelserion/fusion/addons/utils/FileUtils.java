package com.ryderbelserion.fusion.addons.utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FileUtils {

    public static List<Path> getFiles(@NotNull final Path path, @NotNull final List<String> extensions, final int depth) {
        final List<Path> files = new ArrayList<>();

        if (Files.isDirectory(path)) { // check if resolved path is a folder to loop through!
            try {
                Files.walkFileTree(path, new HashSet<>(), Math.max(depth, 1), new SimpleFileVisitor<>() {
                    @Override
                    public @NotNull FileVisitResult visitFile(@NotNull final Path path, @NotNull final BasicFileAttributes attributes) {
                        final String name = path.getFileName().toString();

                        extensions.forEach(extension -> {
                            if (name.endsWith(extension)) {
                                files.add(path);
                            }
                        });

                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (final IOException exception) {
                throw new IllegalStateException("Failed to get a list of files", exception);
            }
        }

        return files;
    }
}