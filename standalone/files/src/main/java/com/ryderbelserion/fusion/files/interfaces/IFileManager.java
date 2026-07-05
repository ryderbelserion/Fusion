package com.ryderbelserion.fusion.files.interfaces;

import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.types.LogCustomFile;
import com.ryderbelserion.fusion.files.types.configurate.JsonCustomFile;
import com.ryderbelserion.fusion.files.types.configurate.YamlCustomFile;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class IFileManager<I> {

    public abstract @NonNull I addFolder(@NonNull final Path folder, @NonNull final String jarFolder, @NonNull final FileType fileType, @NonNull final Consumer<ICustomFile<?, ?, ?>> consumer);

    public @NonNull I addFolder(@NonNull final Path folder, @NonNull final String jarFolder, @NonNull final FileType fileType) {
        return addFolder(folder, jarFolder, fileType, consumer -> consumer.addAction(FileAction.EXTRACT_FOLDER));
    }

    public abstract @NonNull I addFile(@NonNull final Path path, @NonNull final String jarFolder, @NonNull final FileType fileType, @NonNull final Consumer<ICustomFile<?, ?, ?>> consumer);

    public @NonNull I addFile(@NonNull final Path path, @NonNull final String jarFolder, @NonNull final FileType fileType) {
        return addFile(path, jarFolder, fileType, consumer -> consumer.addAction(FileAction.EXTRACT_FILE));
    }

    public @NonNull I addFolder(@NonNull final Path folder, @NonNull final FileType fileType, @NonNull final Consumer<ICustomFile<?, ?, ?>> consumer) {
        return addFolder(folder, "", fileType, consumer);
    }

    public @NonNull I addFolder(@NonNull final Path folder, @NonNull final FileType fileType) {
        return addFolder(folder, fileType, consumer -> consumer.addAction(FileAction.EXTRACT_FOLDER));
    }

    public @NonNull I addFile(@NonNull final Path path, @NonNull final FileType fileType, @NonNull final Consumer<ICustomFile<?, ?, ?>> consumer) {
        return addFile(path, "", fileType, consumer);
    }

    public @NonNull I addFile(@NonNull final Path path, @NonNull final FileType fileType) {
        return addFile(path, fileType, consumer -> consumer.addAction(FileAction.EXTRACT_FILE));
    }

    public abstract @NonNull I addFile(@NonNull final Path path, @NonNull final ICustomFile<?, ?, ?> customFile);

    public abstract @NonNull I removeFile(@NonNull final Path path);

    public abstract @NonNull I reloadFile(@NonNull final Path path);

    public abstract @NonNull I saveFile(@NonNull final Path path);

    public abstract @NonNull I purge();

    public abstract @NonNull I refresh(final boolean save);

    public abstract boolean hasFile(@NonNull final Path path);

    public abstract @NonNull Optional<ICustomFile<?, ?, ?>> getFile(@NonNull final Path path);

    public abstract @NonNull Map<Path, ICustomFile<?, ?, ?>> getFiles();

    public abstract @NonNull YamlCustomFile buildYamlFile(@NonNull final Path path, @NonNull final String jarFolder, @NonNull final Consumer<YamlCustomFile> consumer);

    public abstract @NonNull JsonCustomFile buildJsonFile(@NonNull final Path path, @NonNull final String jarFolder, @NonNull final Consumer<JsonCustomFile> consumer);

    public @NonNull YamlCustomFile buildYamlFile(@NonNull final Path path, @NonNull final Consumer<YamlCustomFile> consumer) {
        return buildYamlFile(path, "", consumer);
    }

    public @NonNull JsonCustomFile buildJsonFile(@NonNull final Path path, @NonNull final Consumer<JsonCustomFile> consumer) {
        return buildJsonFile(path, "", consumer);
    }

    public abstract @NonNull LogCustomFile buildLogFile(@NonNull final Path path, @NonNull final Consumer<LogCustomFile> consumer);

    public @NonNull Optional<YamlCustomFile> getYamlFile(@NonNull final Path path) {
        return getFile(path).map(YamlCustomFile.class::cast);
    }

    public @NonNull Optional<JsonCustomFile> getJsonFile(@NonNull final Path path) {
        return getFile(path).map(JsonCustomFile.class::cast);
    }

    public @NonNull Optional<LogCustomFile> getLogFile(@NonNull final Path path) {
        return getFile(path).map(LogCustomFile.class::cast);
    }

    public abstract @NonNull Optional<JarEntry> getEntry(@NonNull final JarFile jarFile, @NonNull final Predicate<? super JarEntry> predicate);

    public abstract @NonNull I extractFile(@NonNull final String input, @NonNull final Predicate<? super JarEntry> predicate);

    public abstract @NonNull I extractFile(@NonNull final String input, @NonNull final Path output);

    public @NonNull final I extractFile(@NonNull final String input) {
        return extractFile(input, entry -> entry.getName().equalsIgnoreCase(input));
    }

    public abstract @NonNull I extractFolder(@NonNull final String folder, @NonNull final String jarFolder, @NonNull final FileType fileType, @NonNull final Path output);

    public @NonNull final I extractFolder(@NonNull final String folder, @NonNull final FileType fileType, @NonNull final Path output) {
        return extractFolder(folder, "", fileType, output);
    }

    public @NonNull final String parseFolder(@NonNull final String name, @NonNull final String jarFolder) {
        return jarFolder.isBlank() ? name : name.replace("%s%s".formatted(jarFolder, "/"), "");
    }

    public abstract @NonNull I compressFolder(@NonNull final Path path, @NonNull final String content);

    public abstract @NonNull I compressFile(@NonNull final Path path, @Nullable final Path folder, @NonNull final String content);

    public abstract @NonNull I writeFile(@NonNull final Path path, @NonNull final String content);

    public abstract @NonNull List<String> getFileByNames(@NonNull final String folder, @NonNull final Path path, @NonNull final String extension, final int depth, final boolean removeExtension);

    public @NonNull List<String> getFileByNames(@NonNull final String folder, @NonNull final Path path, @NonNull final String extension, final boolean removeExtension) {
        return getFileByNames(folder, path, extension, getDepth(), removeExtension);
    }

    public abstract @NonNull List<Path> getFilesByPath(@NonNull final Path path, @NonNull final List<String> extensions, final int depth);

    public @NonNull List<Path> getFilesByPath(@NonNull final Path path, @NonNull final String extension, final int depth) {
        return getFilesByPath(path, List.of(extension), depth);
    }

    public @NonNull List<Path> getFilesByPath(@NonNull final Path path, @NonNull final List<String> extension) {
        return getFilesByPath(path, extension, getDepth());
    }

    public @NonNull List<Path> getFilesByPath(@NonNull final Path path, @NonNull final String extension) {
        return getFilesByPath(path, List.of(extension), getDepth());
    }

    public int getDirectorySize(@NonNull final Path path, @NonNull final String extension) {
        return getFilesByPath(path, extension, getDepth()).size();
    }

    public @NonNull I compressFile(@NonNull final Path path, @NonNull final String content) {
        return compressFile(path, null, content);
    }

    public @NonNull I compressFile(@NonNull final Path path, @NonNull final Path folder) {
        return compressFile(path, folder, "");
    }

    public @NonNull I compressFile(@NonNull final Path path) {
        return compressFile(path, null, "");
    }

    public abstract void setDepth(final int depth);

    public abstract int getDepth();

    protected String asString(@NonNull final Path path, @NonNull final String content) {
        final StringBuilder builder = new StringBuilder();

        builder.append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        if (!content.isEmpty()) builder.append(content);

        return builder.append("-").append(getDirectorySize(path, ".gz")).append(".gz").toString();
    }
}