package com.ryderbelserion.fusion.core.utils;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
import com.ryderbelserion.fusion.core.files.FileAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
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

    private static final FusionCore fusion = FusionCore.Provider.get();

    private static final Path dataFolder = fusion.getPath();

    private static final ILogger logger = fusion.getLogger();

    /**
     * Extracts a set of files from inside src/main/resources based on the input provided.
     *
     * @param input   the directory in the jar to scan through
     * @param output  the output which where to extract files to
     */
    public static void extract(@NotNull final String input, @NotNull final Path output, @NotNull final List<FileAction> actions) {
        final Path folder = output.resolve(input);

        if (actions.contains(FileAction.DELETE)) {
            try {
                Files.walkFileTree(folder, new SimpleFileVisitor<>() {
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
                });
            } catch (IOException ignored) {}
        }

        if (Files.exists(folder)) {
            return;
        }

        final boolean isFolder = actions.contains(FileAction.FOLDER);

        if (isFolder) {
            try {
                Files.createDirectories(folder);
            } catch (final IOException exception) {
                logger.warn("Failed to create folder {}", folder, exception);
            }
        }

        try (final JarFile jar = new JarFile(Path.of(FusionCore.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toFile())) {
            final Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                final JarEntry entry = entries.nextElement();

                final String entryName = entry.getName();

                if (entryName.endsWith(".class") || entryName.startsWith("META-INF") || !entryName.startsWith(input)) { // exclude .class, and META-INF before checking input
                    continue;
                }

                final boolean isDirectory = entry.isDirectory();

                final Path entryPath = output.resolve(entryName);

                if (isDirectory) {
                    Files.createDirectories(entryPath);

                    continue;
                }

                try (final InputStream stream = jar.getInputStream(entry)) {
                    if (isFolder) {
                        Files.createDirectories(entryPath.getParent());
                    }

                    Files.copy(stream, entryPath);
                }
            }
        } catch (final URISyntaxException | IOException exception) {
            logger.error("Failed to extract {}", folder, exception);
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
        return getNamesByExtension(folder, path, extension, fusion.getRecursionDepth());
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
        return getNamesWithoutExtension(folder, path, extension, fusion.getRecursionDepth());
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
                logger.warn("Failed to get a list of files", exception);
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
        return getFiles(folder.isEmpty() ? path : path.resolve(folder), List.of(extension), fusion.getRecursionDepth());
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
        return getFiles(path, extensions, fusion.getRecursionDepth());
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
        return getFiles(path, List.of(extension), fusion.getRecursionDepth());
    }

    /**
     * Writes the specified content to a file using stream-based operations.
     * This method ensures efficient file handling and data persistence.
     *
     * @param input  the target file where content will be written
     * @param format the data or content to be written into the file
     */
    public static void write(@NotNull final File input, @NotNull final String format) {
        try (final FileWriter writer = new FileWriter(input, true); final BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write(format);
            bufferedWriter.newLine();
            writer.flush();
        } catch (Exception exception) {
            throw new FusionException("Failed to write " + input + " to " + input, exception);
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

        final boolean isDelete = actions.contains(FileAction.DELETE);

        if (Files.isDirectory(path)) {
            try (final ZipOutputStream output = new ZipOutputStream(Files.newOutputStream(file)); final Stream<Path> values = Files.walk(path)) {
                final List<Path> entries = values.filter(key -> !Files.isDirectory(key)).toList();

                for (final Path entry : entries) {
                    final long size = Files.size(entry);

                    if (size > 0) {
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

        if (size > 0) {
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
}