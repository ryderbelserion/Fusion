package com.ryderbelserion.fusion.api;

import com.ryderbelserion.fusion.api.enums.Level;
import com.ryderbelserion.fusion.api.enums.files.enums.FileType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
@NullMarked
public abstract class FusionApi<S, C, TR> {

    @NullUnmarked
    public abstract void compressFile(@NonNull final Path path, @Nullable final Path folder, @NonNull final String content);

    public void compressFile(final Path path, final String content) {
        compressFile(path, null, content);
    }

    public void compressFile(final Path path, final Path folder) {
        compressFile(path, folder, "");
    }

    public void compressFile(final Path path) {
        compressFile(path, null, "");
    }

    public abstract void compressFolder(final Path path, final String content);

    public abstract void extractFolder(final String input, final String jarFolder, final FileType fileType, final Path path);

    public abstract void extractFile(final String input, final Path path);

    public abstract void extractFile(final String input);

    public abstract List<Path> getFilesByPath(
            final Path path,
            final List<String> extensions
    );

    public List<Path> getFilesByPath(
            final Path path,
            final String extensions
    ) {
        return getFilesByPath(path, List.of(extensions));
    }

    public abstract List<String> getFilesByName(
            final String folder,
            final Path path,
            final String extension,
            final int depth,
            final boolean removeExtension
    );

    public List<String> getFilesByName(
            final String folder,
            final Path path,
            final String extension,
            final boolean removeExtension
    ) {
        return getFilesByName(folder, path, extension, getDepth(), removeExtension);
    }

    public abstract String replacePlaceholders(final String message, final Map<String, String> placeholders);

    public abstract String papi(final S sender, final String message);

    public abstract void deleteDirectory(final Path path) throws IOException;

    public abstract Path getDataPath();

    public abstract boolean isVerbose();

    public abstract FusionApi init();

    public abstract FusionApi post();

    public abstract FusionApi reload();

    public abstract int getDepth();

    public abstract void log(
            final Level level,
            final String message,
            final Exception exception,
            final Object... args
    );

    public abstract void log(
            final Level level,
            final String message,
            final Object... args
    );

    public abstract C asComponent(
            final String message,
            final Map<String, String> placeholders,
            final TR... tags
    );

    public C asComponent(
            final S sender,
            final String message,
            final Map<String, String> placeholders,
            final TR... tags
    ) {
        return asComponent(papi(sender, message), placeholders, tags);
    }

    public C asComponent(
            final S audience,
            final String message
    ) {
        return asComponent(audience, message, Map.of());
    }

    public C asComponent(
            final String message
    ) {
        return asComponent(message, Map.of());
    }

    public String parse(
            final S sender,
            final String message,
            final Map<String, String> placeholders
    ) {
        return replacePlaceholders(papi(sender, message), placeholders);
    }

    public String parse(
            final S sender,
            final String message
    ) {
        return parse(sender, message, Map.of());
    }
}