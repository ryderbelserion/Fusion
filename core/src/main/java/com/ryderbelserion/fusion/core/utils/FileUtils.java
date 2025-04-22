package com.ryderbelserion.fusion.core.utils;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
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

public class FileUtils {

    private static final FusionCore api = FusionCore.Provider.get();

    private static final Path dataFolder = api.getDataPath();

    private static final ComponentLogger logger = api.getLogger();

    public static void extract(@NotNull final String input, @NotNull final Path output, final boolean isFolder, final boolean purge) {
        final Path folder = output.resolve(input);

        if (purge) {
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
            throw new FusionException(String.format("Failed to extract %s", input), exception);
        }
    }

    public static List<String> getNamesByExtension(@NotNull final String folder, @NotNull final Path path, @NotNull final String extension, final int depth) {
        final List<Path> files = getFiles(folder.isEmpty() ? path : path.resolve(folder), extension, depth);

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

    public static List<String> getNamesByExtension(@NotNull final String folder, @NotNull final Path path, @NotNull final String extension) {
        return getNamesByExtension(folder, path, extension, api.getRecursionDepth());
    }

    public static List<String> getNamesWithoutExtension(@NotNull final String folder, @NotNull final Path path, @NotNull final String extension, final int depth) {
        final List<Path> files = getFiles(folder.isEmpty() ? path : path.resolve(folder), extension, depth);

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

    public static List<String> getNamesWithoutExtension(@NotNull final String folder, @NotNull final Path path, @NotNull final String extension) {
        return getNamesWithoutExtension(folder, path, extension, api.getRecursionDepth());
    }

    public static List<Path> getFiles(@NotNull final Path path, @NotNull final String extension, final int depth) {
        final List<Path> files = new ArrayList<>();

        if (Files.isDirectory(path)) { // check if resolved path is a folder to loop through!
            try {
                Files.walkFileTree(path, new HashSet<>(), Math.max(depth, 1), new SimpleFileVisitor<>() {
                    @Override
                    public @NotNull FileVisitResult visitFile(@NotNull final Path path, @NotNull final BasicFileAttributes attributes) {
                        final String name = path.getFileName().toString();

                        if (name.endsWith(extension)) {
                            files.add(path);
                        }

                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (final IOException exception) {
                logger.warn("Failed to get list of files.", exception);
            }
        }

        return files;
    }

    public static List<Path> getFiles(@NotNull final String folder, @NotNull final Path path, @NotNull final String extension) {
        return getFiles(folder.isEmpty() ? path : path.resolve(folder), extension, api.getRecursionDepth());
    }

    public static List<Path> getFiles(@NotNull final Path path, @NotNull final String extension) {
        return getFiles(path, extension, api.getRecursionDepth());
    }

    public static void write(@NotNull final File input, @NotNull final String format) {
        try (final FileWriter writer = new FileWriter(input, true); final BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write(format);
            bufferedWriter.newLine();
            writer.flush();
        } catch (Exception exception) {
            throw new FusionException("Failed to write " + input + " to " + input, exception);
        }
    }

    public static void compress(@NotNull final List<Path> paths, @NotNull final Path directory, @NotNull final String content, final boolean purge) throws IOException {
        for (final Path path : paths) {
            compress(path, directory, content, purge);
        }
    }

    public static void compress(@NotNull final Path path, @Nullable final Path directory, @NotNull final String content, final boolean purge) throws IOException {
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

                    if (purge) {
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

        if (purge) {
            Files.deleteIfExists(path);
        }
    }
}