package com.ryderbelserion.fusion.core;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.core.api.registry.mods.ModRegistry;
import com.ryderbelserion.fusion.core.config.FusionConfig;
import com.ryderbelserion.fusion.files.FileManager;
import org.jspecify.annotations.NonNull;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public abstract class FusionCore {

    protected final SettingsManager config;

    private final FileManager fileManager;
    private final Path path;

    public FusionCore(@NonNull final Path path) {
        this.config = SettingsManagerBuilder
                .withYamlFile(path.resolve("fusion.yml"))
                .configurationData(FusionConfig.class)
                .useDefaultMigrationService()
                .create();

        this.fileManager = new FileManager(path);
        this.path = path;
    }

    private ModRegistry modRegistry;

    public abstract void log(
            @NonNull final Level level,
            @NonNull final String message,
            @NonNull final Exception exception,
            @NonNull final Object... args
    );

    public abstract void log(
            @NonNull final Level level,
            @NonNull final String message,
            @NonNull final Object... args
    );

    public abstract boolean isModReady(@NonNull final FusionKey key);

    public FusionCore reload() {
        if (this.config != null) {
            this.config.reload();
        }

        return this;
    }

    public FusionCore init() {
        FusionProvider.register(this);

        if (Files.notExists(this.path)) {
            try {
                Files.createDirectory(this.path);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        this.modRegistry = new ModRegistry();
        this.modRegistry.init();

        return this;
    }

    public @NonNull final List<String> getFilesByName(
            @NonNull final String folder,
            @NonNull final Path path,
            @NonNull final String extension,
            final int depth,
            final boolean removeExtension
    ) {

        return this.fileManager.getFileByNames(folder, path, extension, depth, removeExtension);
    }

    public @NonNull final List<String> getFilesByName(
            @NonNull final String folder,
            @NonNull final Path path,
            @NonNull final String extension,
            final boolean removeExtension
    ) {

        return this.fileManager.getFileByNames(folder, path, extension, removeExtension);
    }

    public @NonNull final List<Path> getFilesByPath(
            @NonNull final Path path,
            @NonNull final List<String> extensions
    ) {
        return this.fileManager.getFilesByPath(path, extensions, getDepth());
    }

    public @NonNull final List<Path> getFilesByPath(
            @NonNull final Path path,
            @NonNull final String extension
    ) {
        return this.fileManager.getFilesByPath(path, extension, getDepth());
    }

    public String replacePlaceholders(@NonNull final String message, @NonNull final Map<String, String> placeholders) {
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

    public void deleteDirectory(@NonNull final Path path) throws IOException {
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

    public @NonNull FileManager getFileManager() {
        return this.fileManager;
    }

    public @NonNull final String getNumberFormat() {
        return this.config.getProperty(FusionConfig.number_format);
    }

    public @NonNull final String getItemsPlugin() {
        return this.config.getProperty(FusionConfig.custom_items_plugin);
    }

    public @NonNull final String getRounding() {
        return this.config.getProperty(FusionConfig.rounding_format);
    }

    public final boolean isVerbose() {
        return this.config.getProperty(FusionConfig.is_verbose);
    }

    public final int getDepth() {
        return this.config.getProperty(FusionConfig.recursion_depth);
    }

    public @NonNull final ModRegistry getModRegistry() {
        return this.modRegistry;
    }

    public @NonNull final Path getDataPath() {
        return this.path;
    }
}