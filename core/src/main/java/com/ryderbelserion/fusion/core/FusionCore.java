package com.ryderbelserion.fusion.core;

import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.interfaces.IFusionCore;
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
import java.util.function.Consumer;

public abstract class FusionCore implements IFusionCore {

    private Path dataPath;

    public FusionCore(@NotNull final Consumer<FusionCore> consumer) {
        consumer.accept(this);
    }

    public abstract FusionCore init();

    @Override
    public List<Path> getFiles(@NotNull final Path path, @NotNull final List<String> extensions, final int depth) {
        final List<Path> files = new ArrayList<>();

        if (Files.notExists(path) || !Files.isDirectory(path)) return new ArrayList<>();

        try {
            Files.walkFileTree(path, new HashSet<>(), Math.max(depth, 1), new SimpleFileVisitor<>() {
                @Override
                public @NotNull FileVisitResult visitFile(@NotNull final Path path, @NotNull final BasicFileAttributes attributes) {
                    final String fileName = path.getFileName().toString();

                    extensions.forEach(extension -> {
                        if (fileName.endsWith(extension)) files.add(path);
                    });

                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (final IOException exception) {
            throw new FusionException("Failed to get a list of files", exception);
        }

        return files;
    }

    @Override
    public List<Path> getFiles(@NotNull final Path path, @NotNull final String extension, final int depth) {
        return getFiles(path, List.of(extension), depth);
    }

    @Override
    public void setDataPath(@NotNull final Path dataPath) {
        this.dataPath = dataPath;
    }

    @Override
    public @NotNull Path getDataPath() {
        return this.dataPath;
    }
}