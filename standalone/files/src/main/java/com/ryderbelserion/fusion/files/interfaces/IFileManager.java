package com.ryderbelserion.fusion.files.interfaces;

import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.types.LogCustomFile;
import com.ryderbelserion.fusion.files.types.configurate.JsonCustomFile;
import com.ryderbelserion.fusion.files.types.configurate.YamlCustomFile;
import org.jspecify.annotations.NullMarked;
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

@NullMarked
public abstract class IFileManager<I> {

    public abstract I addFolder(final Path folder, final String jarFolder, final FileType fileType, final Consumer<ICustomFile<?, ?, ?>> consumer);

    public I addFolder(final Path folder, final String jarFolder, final FileType fileType) {
        return addFolder(folder, jarFolder, fileType, consumer -> consumer.addAction(FileAction.EXTRACT_FOLDER));
    }

    public abstract I addFile(final Path path, final String jarFolder, final FileType fileType, final Consumer<ICustomFile<?, ?, ?>> consumer);

    public I addFile(final Path path, final String jarFolder, final FileType fileType) {
        return addFile(path, jarFolder, fileType, consumer -> consumer.addAction(FileAction.EXTRACT_FILE));
    }

    public I addFolder(final Path folder, final FileType fileType, final Consumer<ICustomFile<?, ?, ?>> consumer) {
        return addFolder(folder, "", fileType, consumer);
    }

    public I addFolder(final Path folder, final FileType fileType) {
        return addFolder(folder, fileType, consumer -> consumer.addAction(FileAction.EXTRACT_FOLDER));
    }

    public I addFile(final Path path, final FileType fileType, final Consumer<ICustomFile<?, ?, ?>> consumer) {
        return addFile(path, "", fileType, consumer);
    }

    public I addFile(final Path path, final FileType fileType) {
        return addFile(path, fileType, consumer -> consumer.addAction(FileAction.EXTRACT_FILE));
    }

    public abstract I addFile(final Path path, final ICustomFile<?, ?, ?> customFile);

    public abstract I removeFile(final Path path);

    public abstract I reloadFile(final Path path);

    public abstract I saveFile(final Path path);

    public abstract I purge();

    public abstract I refresh(final boolean save);

    public abstract boolean hasFile(final Path path);

    public abstract Optional<ICustomFile<?, ?, ?>> getFile(final Path path);

    public abstract Map<Path, ICustomFile<?, ?, ?>> getFiles();

    public abstract YamlCustomFile buildYamlFile(final Path path, final String jarFolder, final Consumer<YamlCustomFile> consumer);

    public abstract JsonCustomFile buildJsonFile(final Path path, final String jarFolder, final Consumer<JsonCustomFile> consumer);

    public YamlCustomFile buildYamlFile(final Path path, final Consumer<YamlCustomFile> consumer) {
        return buildYamlFile(path, "", consumer);
    }

    public JsonCustomFile buildJsonFile(final Path path, final Consumer<JsonCustomFile> consumer) {
        return buildJsonFile(path, "", consumer);
    }

    public abstract LogCustomFile buildLogFile(final Path path, final Consumer<LogCustomFile> consumer);

    public Optional<YamlCustomFile> getYamlFile(final Path path) {
        return getFile(path).map(YamlCustomFile.class::cast);
    }

    public Optional<JsonCustomFile> getJsonFile(final Path path) {
        return getFile(path).map(JsonCustomFile.class::cast);
    }

    public Optional<LogCustomFile> getLogFile(final Path path) {
        return getFile(path).map(LogCustomFile.class::cast);
    }

    public abstract Optional<JarEntry> getEntry(final JarFile jarFile, final Predicate<? super JarEntry> predicate);

    public abstract I extractFile(final String input, final Predicate<? super JarEntry> predicate);

    public abstract I extractFile(final String input, final Path output);

    public final I extractFile(final String input) {
        return extractFile(input, entry -> entry.getName().equalsIgnoreCase(input));
    }

    public abstract I extractFolder(final String folder, final String jarFolder, final FileType fileType, final Path output);

    public final I extractFolder(final String folder, final FileType fileType, final Path output) {
        return extractFolder(folder, "", fileType, output);
    }

    public final String parseFolder(final String name, final String jarFolder) {
        return jarFolder.isBlank() ? name : name.replace("%s%s".formatted(jarFolder, "/"), "");
    }

    public abstract I compressFolder(final Path path, final String content);

    public abstract I compressFile(final Path path, @Nullable final Path folder, final String content);

    public abstract I writeFile(final Path path, final String content);

    public abstract List<String> getFileByNames(final String folder, final Path path, final String extension, final int depth, final boolean removeExtension);

    public List<String> getFileByNames(final String folder, final Path path, final String extension, final boolean removeExtension) {
        return getFileByNames(folder, path, extension, getDepth(), removeExtension);
    }

    public abstract List<Path> getFilesByPath(final Path path, final List<String> extensions, final int depth);

    public List<Path> getFilesByPath(final Path path, final String extension, final int depth) {
        return getFilesByPath(path, List.of(extension), depth);
    }

    public List<Path> getFilesByPath(final Path path, final List<String> extension) {
        return getFilesByPath(path, extension, getDepth());
    }

    public List<Path> getFilesByPath(final Path path, final String extension) {
        return getFilesByPath(path, List.of(extension), getDepth());
    }

    public int getDirectorySize(final Path path, final String extension) {
        return getFilesByPath(path, extension, getDepth()).size();
    }

    public I compressFile(final Path path, final String content) {
        return compressFile(path, null, content);
    }

    public I compressFile(final Path path, final Path folder) {
        return compressFile(path, folder, "");
    }

    public I compressFile(final Path path) {
        return compressFile(path, null, "");
    }

    public abstract void setDepth(final int depth);

    public abstract int getDepth();

    protected String asString(final Path path, final String content) {
        final StringBuilder builder = new StringBuilder();

        builder.append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        if (!content.isEmpty()) builder.append(content);

        return builder.append("-").append(getDirectorySize(path, ".gz")).append(".gz").toString();
    }
}