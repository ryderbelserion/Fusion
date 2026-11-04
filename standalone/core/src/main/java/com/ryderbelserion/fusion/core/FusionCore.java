package com.ryderbelserion.fusion.core;

import com.ryderbelserion.fusion.api.FusionApi;
import com.ryderbelserion.fusion.api.objects.FusionKey;
import com.ryderbelserion.fusion.api.FusionProvider;
import com.ryderbelserion.fusion.core.api.registry.message.MessageRegistry;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileType;
import org.jspecify.annotations.NonNull;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public abstract class FusionCore<S, C> extends FusionApi<S, C> {

    protected final Path configPath;
    protected final Path path;

    public FusionCore(@NonNull final Path path) {
        this.configPath = path.resolve("fusion.yml");
        this.path = path;
    }

    private MessageRegistry messageRegistry;
    private FileManager fileManager;

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

        this.fileManager = new FileManager(this.path);
        /*this.fileManager.addFile(this.configPath, FileType.YAML, action ->
                action.addAction(FileAction.EXTRACT_FILE).addAction(FileAction.KEEP_FILE)).setDepth(getDepth()).setVerbose(isVerbose());*/

        this.messageRegistry = new MessageRegistry(this, FusionKey.key(getNamespace(), "default"));

        return this;
    }

    @Override
    public @NonNull FusionCore post() {
        return this;
    }

    @Override
    public @NonNull FusionCore reload() {
        //this.fileManager.reloadFile(this.configPath).setDepth(getDepth()).setVerbose(isVerbose());

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

    public @NonNull final FileManager getFileManager() {
        return this.fileManager;
    }

    public @NonNull final String getNumberFormat() {
        return "#,###.##";
    }

    public @NonNull final String getItemsPlugin() {
        return "None";
    }

    public @NonNull final String getRounding() {
        return "half_even";
    }

    public final boolean isVerbose() {
        return true;
    }

    public final int getDepth() {
        return 1;
    }

    public @NonNull final MessageRegistry getMessageRegistry() {
        return this.messageRegistry;
    }
}