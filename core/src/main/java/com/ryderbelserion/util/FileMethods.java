package com.ryderbelserion.util;

import com.ryderbelserion.FusionLayout;
import com.ryderbelserion.FusionProvider;
import com.ryderbelserion.api.exception.FusionException;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileMethods {

    private static final FusionLayout api = FusionProvider.get();

    private static final ComponentLogger logger = api.getLogger();

    private static final File dataFolder = api.getDataFolder();

    public static void download(@NotNull final String link, @NotNull final File directory) {
        CompletableFuture.runAsync(() -> {
            URL url;

            try {
                url = URI.create(link).toURL();
            } catch (MalformedURLException exception) {
                throw new FusionException("Failed to download because " + link + " is malformed", exception);
            }

            try (ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream()); FileOutputStream outputStream = new FileOutputStream(directory)) {
                outputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            } catch (IOException exception) {
                throw new FusionException("Failed to download " + link + " to " + directory.getAbsolutePath(), exception);
            }
        });
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

    public static void zip(@NotNull final File input, final boolean purge) {
        zip(List.of(input), null, "", purge);
    }

    public static void zip(@NotNull final File input, @NotNull final String extension, final boolean purge) {
        final List<File> files = getFiles(dataFolder, input.getName(), extension, false);

        if (files.isEmpty()) return;

        boolean hasNonEmptyFile = false;

        for (final File zip : files) {
            if (zip.exists() && zip.length() > 0) {
                hasNonEmptyFile = true;

                break;
            }
        }

        if (!hasNonEmptyFile) {
            return;
        }

        int count = getFiles(input, ".gz", true).size();

        count++;

        zip(files, input, "-" + count, purge);
    }

    public static void zip(@NotNull final List<File> files, @Nullable final File directory, final String extra, final boolean purge) {
        if (files.isEmpty()) return;

        final StringBuilder builder = new StringBuilder();

        builder.append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        if (!builder.isEmpty()) {
            builder.append(extra);
        }

        builder.append(".gz");

        if (directory != null) {
            directory.mkdirs();
        }

        final File zipFile = new File(directory == null ? dataFolder : directory, builder.toString());

        try (final FileOutputStream fileOutputStream = new FileOutputStream(zipFile); ZipOutputStream zipOut = new ZipOutputStream(fileOutputStream)) {
            for (File file : files) {
                if (file.length() > 0) {
                    try (final FileInputStream fileInputStream = new FileInputStream(file)) {
                        final ZipEntry zipEntry = new ZipEntry(file.getName());

                        zipOut.putNextEntry(zipEntry);

                        byte[] bytes = new byte[1024];
                        int length;

                        while ((length = fileInputStream.read(bytes)) >= 0) {
                            zipOut.write(bytes, 0, length);
                        }
                    }

                    if (purge) file.delete();
                }
            }
        } catch (IOException exception) {
            throw new FusionException("Failed to zip " + zipFile, exception);
        }
    }

    public static void extract(@NotNull final String input, @NotNull final String output, final boolean replaceExisting) {
        try {
            visit(path -> {
                final Path directory = dataFolder.toPath().resolve(output);

                try {
                    // Delete and re-create directory if true
                    if (replaceExisting) {
                        directory.toFile().delete();
                    }

                    if (!Files.exists(directory)) {
                        directory.toFile().mkdirs();

                        try (final Stream<Path> files = Files.walk(path)) {
                            files.filter(Files::isRegularFile).forEach(file -> {
                                try {
                                    final Path langFile = directory.resolve(file.getFileName().toString());

                                    if (!Files.exists(langFile)) {
                                        try (final InputStream stream = Files.newInputStream(file)) {
                                            Files.copy(stream, langFile);
                                        }
                                    }
                                } catch (IOException exception) {
                                    throw new FusionException("Failed to extract " + file.getFileName() + " from " + path, exception);
                                }
                            });
                        }
                    }
                } catch (IOException exception) {
                    throw new FusionException("Failed to extract " + input + " to " + output, exception);
                }
            }, input);
        } catch (IOException exception) {
            throw new FusionException("Failed to extract " + input + " to " + output, exception);
        }
    }

    public static void extracts(@Nullable final Class<?> object, @NotNull String input, @Nullable final Path output, final boolean replaceExisting) {
        if (object == null || output == null || input.isEmpty()) return;

        try (JarFile jarFile = new JarFile(Path.of(object.getProtectionDomain().getCodeSource().getLocation().toURI()).toFile())) {
            final String path = input.substring(1);
            final Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                final JarEntry entry = entries.nextElement();
                final String name = entry.getName();

                if (!name.startsWith(path)) continue;

                final Path file = output.resolve(name.substring(path.length()));

                final boolean exists = Files.exists(file);

                if (!replaceExisting && exists) continue;

                if (entry.isDirectory()) {
                    if (!exists) {
                        try {
                            Files.createDirectories(file);
                        } catch (IOException exception) {
                            throw new FusionException("Failed to create directory " + file, exception);
                        }
                    }

                    continue;
                }

                try (final InputStream inputStream = new BufferedInputStream(jarFile.getInputStream(entry)); final OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file.toFile()))) {
                    final byte[] buffer = new byte[4096];
                    int readCount;

                    while ((readCount = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, readCount);
                    }

                    outputStream.flush();
                } catch (IOException exception) {
                    throw new FusionException("Failed to save " + file + " to " + output, exception);
                }
            }
        } catch (IOException | URISyntaxException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void extract(@NotNull final String input, final boolean overwrite) {
        saveResource(input, overwrite, false);
    }

    public static void extract(@NotNull final String input) {
        extract(input, input, false);
    }

    public static void visit(@NotNull final Consumer<Path> consumer, @NotNull final String input) throws IOException {
        final URL resource = FileMethods.class.getClassLoader().getResource("config.yml");

        if (resource == null) {
            throw new FusionException("We are lacking awareness of the files in src/main/resources/" + input);
        }

        final URI path = URI.create(resource.toString().split("!")[0] + "!/");

        try (final FileSystem fileSystem = FileSystems.newFileSystem(path, Map.of("create", "true"))) {
            final Path toVisit = fileSystem.getPath(input);

            if (Files.exists(toVisit)) {
                consumer.accept(toVisit);
            }
        }
    }

    public static List<String> getNames(@NotNull final File directory, @NotNull final String folder, @NotNull final String extension, final boolean keepExtension) {
        return getFiles(directory, folder, extension, keepExtension).stream().map(File::getName).collect(Collectors.toList());
    }

    public static List<String> getNames(@NotNull final File directory, @NotNull final String extension, final boolean keepExtension) {
        return getFiles(directory, extension, keepExtension).stream().map(File::getName).collect(Collectors.toList());
    }

    public static List<File> getFiles(@NotNull final File directory, @NotNull final String folder, @NotNull final String extension, final boolean keepExtension) {
        return getFiles(folder.isEmpty() ? directory : new File(directory, folder), extension, keepExtension);
    }

    public static List<File> getFiles(@NotNull final File directory, @NotNull final String extension, final boolean keepExtension) {
        List<File> files = new ArrayList<>();

        String[] list = directory.list();
        if (list == null) return files;

        File[] array = directory.listFiles();
        if (array == null) return files;

        for (final File file : array) {
            if (file.isDirectory()) {
                final String[] folder = file.list();

                if (folder != null) {
                    for (final String name : folder) {
                        if (!name.endsWith(extension)) continue;

                        files.add(new File(keepExtension ? name : name.replaceAll(extension, "")));
                    }
                }
            } else {
                final String name = file.getName();

                if (!name.endsWith(extension)) continue;

                files.add(new File(keepExtension ? name : name.replaceAll(extension, "")));
            }
        }

        return Collections.unmodifiableList(files);
    }

    public static void saveResource(String resourcePath, final boolean replace, final boolean isVerbose) {
        if (resourcePath == null || resourcePath.isEmpty()) {
            throw new FusionException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        final InputStream inputStream = getResource(resourcePath);

        if (inputStream == null) {
            throw new FusionException("The embedded resource '" + resourcePath + "' cannot be found.");
        }

        File outFile = new File(dataFolder, resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(dataFolder, resourcePath.substring(0, Math.max(lastIndex, 0)));

        if (outDir.mkdirs()) {
            if (isVerbose) logger.warn("Created directory {}", outDir.getAbsolutePath());
        }

        try {
            if (!outFile.exists() || replace) {
                final OutputStream outputStream = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;

                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }

                outputStream.close();
                inputStream.close();
            } else {
                if (isVerbose) logger.warn("Could not save {} to {} because {} already exists", outFile.getName(), outFile, outFile.getName());
            }
        } catch (IOException exception) {
            throw new FusionException("Failed to save " + resourcePath + " to " + outFile.getName(), exception);
        }
    }

    public static InputStream getResource(@NotNull final String path) {
        try {
            final URL url = FileMethods.class.getClassLoader().getResource(path);

            if (url == null) {
                return null;
            }

            final URLConnection connection = url.openConnection();

            connection.setUseCaches(false);

            return connection.getInputStream();
        } catch (IOException exception) {
            throw new FusionException("Failed to get resource path " + path + " out of jar", exception);
        }
    }
}