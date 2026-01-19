package com.ryderbelserion.fusion.core;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.core.config.FusionConfig;
import com.ryderbelserion.fusion.files.FileManager;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FusionCore {

    protected final SettingsManager config;
    private final FileManager fileManager;
    private final Path path;

    public FusionCore(@NotNull final Path source, final Path path) {
        this.config = SettingsManagerBuilder
                .withYamlFile(path.resolve("fusion.yml"))
                .configurationData(FusionConfig.class)
                .useDefaultMigrationService()
                .create();

        this.fileManager = new FileManager(source, path);
        this.path = path;
    }

    public abstract void log(@NotNull final Level level, @NotNull final String message, @NotNull final Map<String, String> placeholders);

    public void log(@NotNull final Level level, @NotNull final String message) {
        this.log(level, message, new HashMap<>());
    }

    public FusionCore reload() {
        if (this.config != null) {
            this.config.reload();
        }

        return this;
    }

    public FusionCore init() {
        if (Files.notExists(this.path)) {
            try {
                Files.createDirectory(this.path);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        return this;
    }

    public @NotNull final List<String> getFilesByName(@NotNull final String folder,
                                                      @NotNull final Path path,
                                                      @NotNull final String extension,
                                                      final int depth,
                                                      final boolean removeExtension) {

        return this.fileManager.getFileByNames(folder, path, extension, depth, removeExtension);
    }

    public @NotNull final List<Path> getFilesByPath(@NotNull final Path path,
                                              @NotNull final List<String> extensions) {
        return this.fileManager.getFilesByPath(path, extensions, 1);
    }

    public String replacePlaceholders(@NotNull final String message, @NotNull final Map<String, String> placeholders) {
        String safeMessage = message;

        if (!placeholders.isEmpty()) {
            for (final Map.Entry<String, String> key : placeholders.entrySet()) {
                if (key == null) continue;

                final String placeholder = key.getKey();
                final String value = key.getValue();

                if (placeholder != null && value != null) {
                    safeMessage = safeMessage.replace(placeholder, value).replace(placeholder.toLowerCase(), value);
                }
            }
        }

        return safeMessage;
    }

    public void deleteDirectory(@NotNull final Path path) throws IOException {
        if (!Files.exists(path) || !Files.isDirectory(path)) return;

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

    public @NotNull final String getNumberFormat() {
        return this.config.getProperty(FusionConfig.number_format);
    }

    public @NotNull final String getItemsPlugin() {
        return this.config.getProperty(FusionConfig.custom_items_plugin);
    }

    public @NotNull final String getRounding() {
        return this.config.getProperty(FusionConfig.rounding_format);
    }

    public final boolean isVerbose() {
        return this.config.getProperty(FusionConfig.is_verbose);
    }

    public final int getDepth() {
        return this.config.getProperty(FusionConfig.recursion_depth);
    }

    public @NotNull final FileManager getFileManager() {
        return this.fileManager;
    }

    public @NotNull final Path getDataPath() {
        return this.path;
    }
}