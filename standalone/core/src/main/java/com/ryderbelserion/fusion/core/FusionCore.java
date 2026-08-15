package com.ryderbelserion.fusion.core;

import com.ryderbelserion.fusion.api.FusionApi;
import com.ryderbelserion.fusion.api.objects.FusionKey;
import com.ryderbelserion.fusion.api.FusionProvider;
import com.ryderbelserion.fusion.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.registry.message.MessageRegistry;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.api.enums.files.enums.FileAction;
import com.ryderbelserion.fusion.api.enums.files.enums.FileType;
import com.ryderbelserion.fusion.core.files.types.configurate.YamlCustomFile;
import com.ryderbelserion.fusion.core.mods.ModRegistry;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class FusionCore<S, C, TR> extends FusionApi<S, C, TR> {

    protected final Path configPath;
    protected final Path path;

    public FusionCore(@NonNull final Path path) {
        this.configPath = path.resolve("fusion.yml");
        this.path = path;
    }

    private MessageRegistry messageRegistry;
    private ModRegistry modRegistry;
    private FileManager fileManager;

    public abstract boolean isModReady(@NonNull final FusionKey key);

    public abstract boolean isModReady(@NonNull final String key);

    public abstract @NonNull String getNamespace();

    @Override
    public @NonNull ModRegistry getModRegistry() {
        return this.modRegistry;
    }

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
        this.fileManager.addFile(this.configPath, FileType.YAML, action -> action.addAction(FileAction.EXTRACT_FILE).addAction(FileAction.KEEP_FILE)).setDepth(getDepth());

        this.messageRegistry = new MessageRegistry(this, FusionKey.key(getNamespace(), "default"));

        this.modRegistry = new ModRegistry();
        this.modRegistry.init();

        return this;
    }

    @Override
    public @NonNull FusionCore post() {
        return this;
    }

    @Override
    public @NonNull FusionCore reload() {
        this.fileManager.reloadFile(this.configPath).setDepth(getDepth());

        return this;
    }

    @Override
    @NullUnmarked
    public void compressFile(@NonNull final Path path, @Nullable final Path folder, @NonNull final String content) {
        this.fileManager.compressFile(path, folder, content);
    }

    @Override
    public void compressFolder(@NonNull final Path path, @NonNull final String content) {
        this.fileManager.compressFolder(path, content);
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

    public @NonNull final CommentedConfigurationNode getFusionConfig() {
        final Optional<YamlCustomFile> customFile = this.fileManager.getYamlFile(this.configPath);

        if (customFile.isEmpty()) {
            throw new FusionException("Could not find custom file for " + this.configPath);
        }

        return customFile.get().getConfiguration();
    }

    public @NonNull final FileManager getFileManager() {
        return this.fileManager;
    }

    public @NonNull final String getNumberFormat() {
        return getFusionConfig().node("settings", "number_format").getString("#,###.##");
    }

    public @NonNull final String getItemsPlugin() {
        return getFusionConfig().node("settings", "custom-items-plugin").getString("None");
    }

    public @NonNull final String getRounding() {
        return getFusionConfig().node("settings", "rounding").getString("half_even");
    }

    public final boolean isVerbose() {
        return getFusionConfig().node("settings", "is_verbose").getBoolean(false);
    }

    public final int getDepth() {
        return getFusionConfig().node("settings", "recursion_depth").getInt(1);
    }

    public @NonNull final MessageRegistry getMessageRegistry() {
        return this.messageRegistry;
    }
}