package com.ryderbelserion.fusion.core.api.utils;

import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.api.FusionCore;
import com.ryderbelserion.fusion.core.api.enums.FileAction;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * A set of utilities that make extracting or retrieving information about files much easier
 */
public class FileUtils {

    private static final FusionCore fusion = FusionProvider.get();

    private static final Path dataFolder = fusion.getPath();

    /**
     * Extracts a set of files from inside src/main/resources based on the input provided.
     *
     * @param input   the directory in the jar to scan through
     * @param output  the output which where to extract files to
     */
    public static void extract(@NotNull final String input, @NotNull final Path output, @NotNull final List<FileAction> actions) {
        final Path content = output.resolve(input);

        if (actions.contains(FileAction.DELETE_FILE)) {
            try {
                Files.walkFileTree(content, new SimplePathVisitor());
            } catch (final IOException ignored) {}
        }

        if (Files.exists(content)) {
            return;
        }

        final boolean isFolder = actions.contains(FileAction.EXTRACT_FOLDER);

        if (isFolder && Files.isDirectory(content)) {
            try {
                Files.createDirectory(content);
            } catch (final IOException exception) {
                throw new FusionException("Failed to create folder " + content, exception);
            }
        }

        final String parentName = content.getParent().getFileName().toString();
        final String fileName = content.getFileName().toString();
        final Path value = Paths.get(parentName).resolve(fileName);

        final boolean isFile = actions.contains(FileAction.EXTRACT_FILE);

        final String text = isFile ? value.toString().replace("\\", "/") : input;

        try (final JarFile jar = new JarFile(Path.of(FusionCore.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toFile())) {
            final Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                final JarEntry entry = entries.nextElement();

                final String entryName = entry.getName();

                if (entryName.endsWith(".class") || entryName.startsWith("META-INF")) { // exclude .class, and META-INF
                    continue;
                }

                if (isFile) {
                    if (!entryName.equals(text)) {
                        continue;
                    }
                } else {
                    if (!entryName.startsWith(text)) {
                        continue;
                    }
                }

                final boolean isDirectory = entry.isDirectory();

                final Path target = isFile ? output.resolve(input) : output.resolve(entryName);
                
                if (isDirectory) {
                    Files.createDirectories(target);

                    continue;
                }

                try (final InputStream stream = jar.getInputStream(entry)) {
                    final Path parent = target.getParent();

                    if (isFile || isFolder && !Files.exists(parent)) {
                        Files.createDirectories(parent);
                    }

                    Files.copy(stream, target);
                }
            }
        } catch (final URISyntaxException | IOException exception) {
            fusion.log("warn", "Failed to extract file {} to {}! Exception: {}", text, content, exception);
        }
    }

    /**
     * Retrieves a list of file names from the specified folder,
     * This method searches up to the specified depth within the directory structure.
     *
     * @param folder    the directory to scan for files
     * @param path      the relative path which is where your folder would be
     * @param extension the file extension to be searched for (e.g., ".yml")
     * @param depth     the maximum depth level to search within subdirectories
     * @return a list of file names without the specified extension
     */
    public static List<String> getNamesByExtension(@NotNull final String folder, @NotNull final Path path, @NotNull final String extension, final int depth) {
        final List<Path> files = getFiles(folder.isEmpty() ? path : path.resolve(folder), List.of(extension), depth);

        final List<String> names = new ArrayList<>();

        for (final Path file : files) {
            final String name = file.getFileName().toString();

            if (!name.endsWith(extension)) {
                continue;
            }

            names.add(name);
        }

        return names;
    }

    /**
     * Retrieves a list of file names from the specified folder,
     * This method searches up to the specified depth within the directory structure.
     *
     * @param folder    the directory to scan for files
     * @param path      the relative path which is where your folder would be
     * @param extension the file extension to be searched for (e.g., ".yml")
     * @return a list of file names without the specified extension
     */
    public static List<String> getNamesByExtension(@NotNull final String folder, @NotNull final Path path, @NotNull final String extension) {
        return getNamesByExtension(folder, path, extension, fusion.getDepth());
    }

    /**
     * Retrieves a list of file names from the specified folder, excluding the given extension.
     * This method searches up to the specified depth within the directory structure.
     *
     * @param folder    the directory to scan for files
     * @param path      the relative path which is where your folder would be
     * @param extension the file extension to be stripped from the file names (e.g., ".yml")
     * @param depth     the maximum depth level to search within subdirectories
     * @return a list of file names without the specified extension
     */
    public static List<String> getNamesWithoutExtension(@NotNull final String folder, @NotNull final Path path, @NotNull final String extension, final int depth) {
        final List<Path> files = getFiles(folder.isEmpty() ? path : path.resolve(folder), List.of(extension), depth);

        final List<String> names = new ArrayList<>();

        for (final Path file : files) {
            final String name = file.getFileName().toString().replace(extension, "");

            if (names.contains(name)) {
                continue;
            }

            names.add(name);
        }

        return names;
    }

    /**
     * Retrieves a list of file names from the specified folder, which is filtered based on the extension.
     *
     * @param folder    the directory to scan for files
     * @param path      the relative path which is where your folder would be
     * @param extension the file extension to be stripped from the file names (e.g., ".yml")
     * @return a list of files without the extension
     */
    public static List<String> getNamesWithoutExtension(@NotNull final String folder, @NotNull final Path path, @NotNull final String extension) {
        return getNamesWithoutExtension(folder, path, extension, fusion.getDepth());
    }

    /**
     * Retrieves a list of paths from the relative path, including the given extensions.
     * This method searches up to the specified depth within the directory structure.
     *
     * @param path the directory to scan for files
     * @param extensions the list of file extensions to be searched for (e.g., ".yml")
     * @param depth the maximum depth level to search within subdirectories
     * @return a list of files
     */
    public static List<Path> getFiles(@NotNull final Path path, @NotNull final List<String> extensions, final int depth) {
        final List<Path> files = new ArrayList<>();

        if (Files.isDirectory(path)) { // check if resolved path is a folder to loop through!
            try {
                Files.walkFileTree(path, new HashSet<>(), Math.max(depth, 1), new SimpleFileVisitor<>() {
                    @Override
                    public @NotNull FileVisitResult visitFile(@NotNull final Path path, @NotNull final BasicFileAttributes attributes) {
                        final String name = path.getFileName().toString();

                        extensions.forEach(extension -> {
                            if (name.endsWith(extension)) {
                                files.add(path);
                            }
                        });

                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (final IOException exception) {
                throw new FusionException("Failed to get a list of files", exception);
            }
        }

        return files;
    }

    /**
     * Retrieves a list of paths from the relative path, including the given extension.
     * This method searches up to the specified depth within the directory structure.
     *
     * @param folder the directory to scan for files
     * @param path the relative parent path
     * @param extension the file extension to be searched for (e.g., ".yml")
     * @return a list of files
     */
    public static List<Path> getFiles(@NotNull final String folder, @NotNull final Path path, @NotNull final String extension) {
        return getFiles(folder.isEmpty() ? path : path.resolve(folder), List.of(extension), fusion.getDepth());
    }

    /**
     * Retrieves a list of paths from the relative path, including the given extension.
     * This method searches up to the specified depth within the directory structure.
     *
     * @param path the directory to scan for files
     * @param extension the file extension to be searched for (e.g., ".yml")
     * @param depth the maximum depth level to search within subdirectories
     * @return a list of files
     */
    public static List<Path> getFiles(@NotNull final Path path, @NotNull final String extension, final int depth) {
        return getFiles(path, List.of(extension), depth);
    }

    /**
     * Retrieves a list of paths from the relative path, including the given extension.
     * This method searches up to the specified depth within the directory structure.
     *
     * @param path the directory to scan for files
     * @param extensions the file extensions to be searched for (e.g., ".yml")
     * @return a list of files
     */
    public static List<Path> getFiles(@NotNull final Path path, @NotNull final List<String> extensions) {
        return getFiles(path, extensions, fusion.getDepth());
    }

    /**
     * Retrieves a list of paths from the relative path, including the given extension.
     * This method searches up to the specified depth within the directory structure.
     *
     * @param path the directory to scan for files
     * @param extension the file extension to be searched for (e.g., ".yml")
     * @return a list of files
     */
    public static List<Path> getFiles(@NotNull final Path path, @NotNull final String extension) {
        return getFiles(path, List.of(extension), fusion.getDepth());
    }

    /**
     * Writes the specified content to the path file.
     *
     * @param path  the target path where content will be written
     * @param format the data or content to be written
     */
    public static void write(@NotNull final Path path, @NotNull final String format) {
        try {
            Files.writeString(path, format, StandardOpenOption.APPEND);
        } catch (IOException exception) {
            throw new FusionException(String.format("Failed to write %s to %s", format, path), exception);
        }
    }

    /**
     * Compresses multiple files into zip folders.
     *
     * @param paths        the list of relative paths to compress
     * @param directory    the directory where the zip file goes
     * @param content      the optional string to append to the {@link StringBuilder}
     * @param actions      the list of actions
     * @throws IOException throws if anything breaks along the way
     */
    public static void compress(@NotNull final List<Path> paths, @NotNull final Path directory, @NotNull final String content, @NotNull final List<FileAction> actions) throws IOException {
        for (final Path path : paths) {
            compress(path, directory, content, actions);
        }
    }

    /**
     * Deletes a directory, which never stops until all files are deleted!
     *
     * @param path the path to check
     * @throws IOException throws if anything breaks along the way
     */
    public static void deleteDirectory(@NotNull final Path path) throws IOException {
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            return;
        }

        try (final DirectoryStream<Path> contents = Files.newDirectoryStream(path)) {
            for (final Path entry : contents) {
                if (Files.isDirectory(entry)) {
                    deleteDirectory(entry);
                } else {
                    Files.delete(entry);
                }
            }
        }

        Files.deleteIfExists(path);
    }

    /**
     * Compresses multiple files into zip folders.
     *
     * @param path         the relative path to compress
     * @param directory    the directory where the zip file goes
     * @param content      the optional string to append to the {@link StringBuilder}
     * @param actions      the list of actions
     * @throws IOException throws if anything breaks along the way
     */
    public static void compress(@NotNull final Path path, @Nullable final Path directory, @NotNull final String content, @NotNull final List<FileAction> actions) throws IOException {
        if (!Files.exists(path)) {
            return;
        }

        final StringBuilder builder = new StringBuilder();

        builder.append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        if (!content.isEmpty()) {
            builder.append(content);
        }

        builder.append(".gz");

        final Path file = directory == null ? dataFolder : directory.resolve(builder.toString());

        final boolean isDelete = actions.contains(FileAction.DELETE_FILE);

        if (Files.isDirectory(path)) {
            try (final ZipOutputStream output = new ZipOutputStream(Files.newOutputStream(file)); final Stream<Path> values = Files.walk(path)) {
                final List<Path> entries = values.filter(key -> !Files.isDirectory(key)).toList();

                for (final Path entry : entries) {
                    final long size = Files.size(entry);

                    if (size > 0L) {
                        final ZipEntry zipEntry = new ZipEntry(entry.toString());

                        output.putNextEntry(zipEntry);

                        Files.copy(entry, output);

                        output.closeEntry();
                    }

                    if (isDelete) {
                        Files.deleteIfExists(entry);
                    }
                }
            }

            return;
        }

        final long size = Files.size(path);

        if (size > 0L) {
            try (final ZipOutputStream output = new ZipOutputStream(Files.newOutputStream(file))) {
                final ZipEntry zipEntry = new ZipEntry(path.toString());

                output.putNextEntry(zipEntry);

                Files.copy(path, output);

                output.closeEntry();
            }
        }

        if (isDelete) {
            Files.deleteIfExists(path);
        }
    }

    private static class SimplePathVisitor extends SimpleFileVisitor<Path> {
        @Override
        public @NotNull FileVisitResult visitFile(@NotNull final Path file, @NotNull final BasicFileAttributes attributes) throws IOException {
            Files.delete(file);

            return FileVisitResult.CONTINUE;
        }

        @Override
        public @NotNull FileVisitResult postVisitDirectory(@NotNull final Path directory, final IOException exception) throws IOException {
            Files.delete(directory);

            return FileVisitResult.CONTINUE;
        }
    }
}