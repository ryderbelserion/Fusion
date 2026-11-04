package com.ryderbelserion.fusion.core;

import com.ryderbelserion.fusion.api.FusionApi;
import com.ryderbelserion.fusion.api.objects.FusionKey;
import com.ryderbelserion.fusion.api.FusionProvider;
import com.ryderbelserion.fusion.config.properties.PropertyManager;
import com.ryderbelserion.fusion.core.api.registry.message.MessageRegistry;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.impl.FusionConfig;
import com.ryderbelserion.fusion.impl.keys.RootKeys;
import org.jspecify.annotations.NonNull;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public abstract class FusionCore<S, C> extends FusionApi<S, C, FileManager> {

    protected final Path path;

    public FusionCore(@NonNull final Path path) {
        this.path = path;
    }

    private MessageRegistry messageRegistry;
    private FileManager fileManager;
    private PropertyManager config;
    private FusionConfig provider;

    public abstract @NonNull String getNamespace();

    @Override
    public @NonNull FusionCore init() {
        FusionProvider.register(this);

        if (Files.notExists(this.path)) {
            try {
                Files.createDirectory(this.path);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        this.provider = new FusionConfig(this.path);
        this.provider.build();

        this.config = this.provider.getConfig();

        this.fileManager = new FileManager(this.path);

        this.messageRegistry = new MessageRegistry(this, FusionKey.key(getNamespace(), "default"));

        return this;
    }

    @Override
    public @NonNull FusionCore post() {
        return this;
    }

    @Override
    public @NonNull FusionCore reload() {
        this.provider.reload();

        return this;
    }

    @Override
    public void extractFolder(@NonNull final String input, @NonNull final String jarFolder, @NonNull final FileType fileType, @NonNull final Path path) {
        this.fileManager.extractFolder(input, jarFolder, fileType, path);
    }

    @Override
    public void extractFile(@NonNull final String input, @NonNull final Path path) {
        this.fileManager.extractFile(input, path);
    }

    @Override
    public void extractFile(@NonNull final String input) {
        this.fileManager.extractFile(input);
    }

    @Override
    public @NonNull final List<String> getFilesByName(
            @NonNull final String folder,
            @NonNull final Path path,
            @NonNull final String extension,
            final int depth,
            final boolean removeExtension
    ) {
        return this.fileManager.getFileByNames(folder, path, extension, depth, removeExtension);
    }

    @Override
    public @NonNull final List<Path> getFilesByPath(
            @NonNull final Path path,
            @NonNull final List<String> extensions
    ) {
        return this.fileManager.getFilesByPath(path, extensions, getDepth());
    }

    @Override
    public @NonNull final String replacePlaceholders(@NonNull final String message, @NonNull final Map<String, String> placeholders) {
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

    @Override
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

    @Override
    public @NonNull final Path getDataPath() {
        return this.path;
    }

    @Override
    public @NonNull final FileManager getFileManager() {
        return this.fileManager;
    }

    @Override
    public @NonNull final String getNumberFormat() {
        return this.config.getProperty(RootKeys.number_format);
    }

    @Override
    public @NonNull final String getItemsPlugin() {
        return this.config.getProperty(RootKeys.custom_items_plugin);
    }

    @Override
    public @NonNull final String getRounding() {
        return this.config.getProperty(RootKeys.rounding);
    }

    @Override
    public final boolean isVerbose() {
        return this.config.getProperty(RootKeys.is_verbose);
    }

    @Override
    public final int getDepth() {
        return this.config.getProperty(RootKeys.recursion_depth);
    }

    public @NonNull final MessageRegistry getMessageRegistry() {
        return this.messageRegistry;
    }
}