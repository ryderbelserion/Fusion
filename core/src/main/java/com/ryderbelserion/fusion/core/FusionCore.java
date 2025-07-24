package com.ryderbelserion.fusion.core;

import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.interfaces.IFusionCore;
import com.ryderbelserion.fusion.core.api.support.ModManager;
import com.ryderbelserion.fusion.core.utils.StringUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class FusionCore implements IFusionCore {

    private final StringUtils stringUtils;
    private final ModManager modManager;

    protected ComponentLogger logger;
    protected FusionConfig config;
    protected Path dataPath;

    public FusionCore(@NotNull final Consumer<FusionCore> consumer) {
        consumer.accept(this);

        this.stringUtils = new StringUtils();
        this.modManager = new ModManager(this);
    }

    public abstract Component parse(@NotNull final Audience audience, @NotNull final String message, @NotNull final Map<String, String> placeholders, @NotNull final List<TagResolver> tags);

    public Component parse(@NotNull final Audience audience, @NotNull final String message, @NotNull final Map<String, String> placeholders) {
        return parse(audience, message, placeholders, List.of());
    }

    public Component parse(@NotNull final Audience audience, @NotNull final String message) {
        return parse(audience, message, new HashMap<>());
    }

    public abstract String papi(@NotNull final Audience audience, @NotNull final String message);

    public abstract FusionCore init(@NotNull final Consumer<FusionCore> consumer);

    public abstract boolean isModReady(@NotNull final Key key);

    public abstract FusionCore reload();

    @SuppressWarnings("DuplicatedCode")
    public void log(@NotNull final String type, @NotNull final String message, @NotNull final Throwable throwable) {
        if (!this.config.isVerbose()) return;

        final Component component = parse(Audience.audience(), message);

        switch (type) {
            case "info" -> this.logger.info(component, throwable);
            case "error" -> this.logger.error(component, throwable);
            case "warn" -> this.logger.warn(component, throwable);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    public void log(@NotNull final String type, @NotNull final String message, @NotNull final Object... args) {
        if (!this.config.isVerbose()) return;

        final Component component = parse(Audience.audience(), message);

        switch (type) {
            case "info" -> this.logger.info(component, args);
            case "error" -> this.logger.error(component, args);
            case "warn" -> this.logger.warn(component, args);
        }
    }

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
    public List<String> getFileNames(@NotNull final String folder, @NotNull final Path path, @NotNull final String extension, final int depth, final boolean withoutExtension) {
        final List<Path> files = getFiles(folder.isEmpty() ? path : path.resolve(folder), List.of(extension), depth);

        final List<String> names = new ArrayList<>();

        for (final Path key : files) {
            final String fileName = key.getFileName().toString();

            if (!fileName.endsWith(extension)) {
                continue;
            }

            names.add(withoutExtension ? fileName.replace(extension, "") : fileName);
        }

        return names;
    }

    @Override
    public void deleteDirectory(@NotNull final Path path) throws IOException {
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            return;
        }

        try (final DirectoryStream<Path> contents = Files.newDirectoryStream(path)) {
            for (final Path entry : contents) {
                if (Files.isDirectory(entry)) {
                    deleteDirectory(entry);

                    continue;
                }

                Files.delete(entry);
            }
        }

        Files.deleteIfExists(path);
    }

    @Override
    public ModManager getModManager() {
        return this.modManager;
    }

    @Override
    public StringUtils getStringUtils() {
        return this.stringUtils;
    }

    @Override
    public void setDataPath(@NotNull final Path dataPath) {
        if (this.dataPath != null) return;

        this.dataPath = dataPath;
    }

    @Override
    public @NotNull Path getDataPath() {
        return this.dataPath;
    }
}