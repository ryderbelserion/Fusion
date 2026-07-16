package com.ryderbelserion.fusion.files;

import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.interfaces.ICustomFile;
import com.ryderbelserion.fusion.files.interfaces.IFileManager;
import com.ryderbelserion.fusion.files.types.LogCustomFile;
import com.ryderbelserion.fusion.files.types.configurate.JsonCustomFile;
import com.ryderbelserion.fusion.files.types.configurate.YamlCustomFile;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileManager extends IFileManager<FileManager> {

    protected final Map<Path, ICustomFile<?, ?, ?>> files = new HashMap<>();

    private final Path path;
    private int depth = 1;

    public FileManager(@NonNull final Path path) {
        this.path = path;
    }

    @Override
    public @NonNull FileManager addFolder(@NonNull final Path folder, @NonNull final String jarFolder, @NonNull final FileType fileType, @NonNull final Consumer<ICustomFile<?, ?, ?>> consumer) {
        extractFolder(folder.getFileName().toString(), jarFolder, fileType, folder.getParent());

        for (final Path path : getFilesByPath(folder, fileType.getExtension(), getDepth())) {
            addFile(path, fileType, consumer);
        }

        return this;
    }

    @Override
    public @NonNull FileManager addFile(@NonNull final Path path, @NonNull final String jarFolder, @NonNull final FileType fileType, @NonNull final Consumer<ICustomFile<?, ?, ?>> consumer) {
        if (this.files.containsKey(path)) {
            final ICustomFile<?, ?, ?> customFile = this.files.get(path);

            consumer.accept(customFile);

            if (customFile.hasAction(FileAction.DELETE_FILE) && !customFile.hasAction(FileAction.KEEP_FILE)) {
                try {
                    Files.deleteIfExists(path);
                } catch (final IOException exception) {
                    throw new FileException("Failed to delete %s".formatted(path), exception);
                }

                return this;
            }

            if (customFile.hasAction(FileAction.RELOAD_FILE)) {
                customFile.load();
            }

            return this;
        }

        ICustomFile<?, ?, ?> customFile = null;

        switch (fileType) {
            case YAML -> customFile = buildYamlFile(path, jarFolder, consumer::accept);
            case JSON -> customFile = buildJsonFile(path, jarFolder, consumer::accept);
            case LOG -> customFile = buildLogFile(path, consumer::accept);
        }

        if (customFile == null) return this;

        this.files.putIfAbsent(path, customFile);

        return this;
    }

    @Override
    public @NonNull FileManager removeFile(@NonNull final Path path) {
        final Optional<ICustomFile<?, ?, ?>> variable = getFile(path);

        if (variable.isEmpty()) {
            return this;
        }

        if (variable.get().hasAction(FileAction.KEEP_FILE)) {
            return this;
        }

        this.files.remove(path);

        return this;
    }

    @Override
    public @NonNull FileManager purge() {
        final Map<Path, ICustomFile<?, ?, ?>> files = new HashMap<>(this.files);

        for (final Map.Entry<Path, ICustomFile<?, ?, ?>> entry : files.entrySet()) {
            removeFile(entry.getKey());
        }

        return this;
    }

    @Override
    public @NonNull FileManager addFile(@NonNull final Path path, @NonNull final ICustomFile<?, ?, ?> customFile) {
        this.files.putIfAbsent(path, customFile);

        return this;
    }

    @Override
    public @NonNull YamlCustomFile buildYamlFile(@NonNull final Path path, @NonNull final String jarFolder, @NonNull final Consumer<YamlCustomFile> consumer) {
        return new YamlCustomFile(this, jarFolder, path, consumer).load();
    }

    @Override
    public @NonNull JsonCustomFile buildJsonFile(@NonNull final Path path, @NonNull final String jarFolder, @NonNull final Consumer<JsonCustomFile> consumer) {
        return new JsonCustomFile(this, jarFolder, path, consumer).load();
    }

    @Override
    public @NonNull LogCustomFile buildLogFile(@NonNull final Path path, @NonNull final Consumer<LogCustomFile> consumer) {
        return new LogCustomFile(this, path, consumer).load();
    }

    @Override
    public @NonNull FileManager reloadFile(@NonNull final Path path) {
        final Optional<ICustomFile<?, ?, ?>> customFile = getFile(path);

        if (customFile.isEmpty()) return this;

        customFile.get().load();

        return this;
    }

    @Override
    public @NonNull FileManager saveFile(@NonNull final Path path) {
        final Optional<ICustomFile<?, ?, ?>> customFile = getFile(path);

        if (customFile.isEmpty()) return this;

        customFile.get().save();

        return this;
    }

    @Override
    public @NonNull Optional<ICustomFile<?, ?, ?>> getFile(@NonNull final Path path) {
        return Optional.ofNullable(this.files.get(path));
    }

    @Override
    public @NonNull FileManager extractFile(@NonNull final String input, @NonNull final Predicate<? super JarEntry> predicate) {
        final Path output = this.path.resolve(input);

        if (Files.exists(output)) {
            return this;
        }

        final Path parent = output.getParent();

        if (Files.notExists(parent)) {
            try {
                Files.createDirectory(parent);
            } catch (final IOException exception) {
                throw new FileException("Failed to create %s".formatted(parent));
            }
        }

        try (final JarFile jarFile = new JarFile(Path.of(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).toFile())) {
            getEntry(jarFile, predicate).ifPresentOrElse(entry -> {
                if (Files.exists(output)) {
                    return;
                }

                try (final InputStream stream = jarFile.getInputStream(entry)) {
                    Files.copy(stream, output);
                } catch (final IOException exception) {
                    throw new FileException("Failed to copy %s to %s".formatted(input, output), exception);
                }
            }, () -> {
                throw new FileException("Failed to find %s in the jar!".formatted(input));
            });
        } catch (final IOException | URISyntaxException exception) {
            exception.printStackTrace();
        }

        return this;
    }

    @Override
    public @NonNull Optional<JarEntry> getEntry(@NonNull final JarFile jarFile, @NonNull final Predicate<? super JarEntry> predicate) {
        JarEntry target = null;

        for (final JarEntry entry : Collections.list(jarFile.entries())) {
            final String entryName = entry.getName();

            if (entryName.endsWith(".class") || entryName.startsWith("META-INF") || entryName.equalsIgnoreCase("velocity-plugin.json")) continue;
            if (!predicate.test(entry)) continue;

            target = entry;

            break;
        }

        return Optional.ofNullable(target);
    }

    @Override
    public @NonNull FileManager extractFile(@NonNull final String input, @NonNull final Path output) {
        if (Files.exists(output)) {
            return this;
        }

        final Path parent = output.getParent();

        if (Files.notExists(parent)) {
            try {
                Files.createDirectory(parent);
            } catch (final IOException exception) {
                throw new FileException("Failed to create %s".formatted(parent));
            }
        }

        try (final JarFile jarFile = new JarFile(Path.of(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).toFile())) {
            JarEntry target = null;

            for (final JarEntry entry : Collections.list(jarFile.entries())) {
                final String entryName = entry.getName();

                if (entryName.endsWith(".class") || entryName.startsWith("META-INF") || entryName.equalsIgnoreCase("velocity-plugin.json") || entry.isDirectory()) continue;

                if (!entryName.equalsIgnoreCase(input)) continue;

                target = entry;

                break;
            }

            if (target == null) {
                return this;
            }

            if (Files.exists(output)) {
                return this;
            }

            try (final InputStream stream = jarFile.getInputStream(target)) {
                Files.copy(stream, output);
            } catch (final IOException exception) {
                throw new FileException("Failed to copy %s to %s".formatted(input, output), exception);
            }
        } catch (final IOException | URISyntaxException exception) {
            exception.printStackTrace();
        }

        return this;
    }

    @Override
    public @NonNull FileManager extractFolder(@NonNull final String folder, @NonNull final String jarFolder, @NonNull final FileType fileType, @NonNull final Path output) {
        final Path path = output.resolve(folder);

        if (Files.exists(path)) { // do not extract if path exists.
            return this;
        }

        if (Files.notExists(path)) { // create folder
            try {
                Files.createDirectories(path);
            } catch (final Exception exception) {
                throw new FileException("Failed to create %s".formatted(path), exception);
            }
        }

        try (final JarFile jar = new JarFile(Path.of(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).toFile())) {
            final Set<JarEntry> entries = jar.stream().filter(entry -> !entry.getName().endsWith(".class"))
                    .filter(entry -> !entry.getName().startsWith("META-INF"))
                    .filter(entry -> !entry.isDirectory())
                    .filter(entry -> !entry.getName().equalsIgnoreCase("velocity-plugin.json"))
                    .filter(entry -> entry.getName().startsWith(jarFolder.isBlank() ? folder : jarFolder))
                    .filter(entry -> entry.getName().endsWith(fileType.getExtension()))
                    .collect(Collectors.toSet());

            entries.forEach(entry -> {
                final Path target = output.resolve(parseFolder(entry.getName(), jarFolder));

                final Path parent = target.getParent();

                if (Files.notExists(parent)) {
                    try {
                        Files.createDirectories(parent);
                    } catch (final IOException exception) {
                        throw new FileException("Failed to create %s".formatted(parent), exception);
                    }
                }

                if (Files.notExists(target)) {
                    try (final InputStream stream = jar.getInputStream(entry)) {
                        Files.copy(stream, target);
                    } catch (final IOException exception) {
                        throw new FileException("Failed to copy %s to %s".formatted(target, parent), exception);
                    }
                }
            });
        } catch (final IOException | URISyntaxException exception) {
            throw new FileException("Failed to extract folder %s".formatted(path), exception);
        }

        return this;
    }

    @Override
    public @NonNull FileManager compressFolder(@NonNull final Path path, @NonNull final String content) {
        if (!Files.exists(path)) return this;
        if (!Files.isDirectory(path)) return this;

        final Path target = this.path.resolve(asString(path, content));

        try (final ZipOutputStream output = new ZipOutputStream(Files.newOutputStream(target)); final Stream<Path> values = Files.walk(path)) {
            final List<Path> entries = values.filter(key -> !Files.isDirectory(key)).toList();

            for (final Path entry : entries) {
                if (Files.size(entry) <= 0L) continue;

                final ZipEntry zipEntry = new ZipEntry(entry.toString());

                output.putNextEntry(zipEntry);

                Files.copy(entry, output);

                output.closeEntry();
            }
        } catch (final IOException exception) {
            throw new FileException("Failed to compress folder %s".formatted(path), exception);
        }

        return this;
    }

    @Override
    public @NonNull FileManager compressFile(@NonNull final Path path, @Nullable final Path folder, @NonNull final String content) {
        if (!Files.exists(path)) return this;

        long size;

        try {
            size = Files.size(path);
        } catch (final Exception exception) {
            throw new FileException("Failed to calculate file size for %s".formatted(path), exception);
        }

        if (size <= 0L) return this;

        final String builder = asString(path, content);

        final Path target = folder == null ? this.path : folder.resolve(builder);

        try (final ZipOutputStream output = new ZipOutputStream(Files.newOutputStream(target))) {
            final ZipEntry entry = new ZipEntry(path.getFileName().toString());

            output.putNextEntry(entry);

            Files.copy(path, output);

            output.closeEntry();
        } catch (final Exception exception) {
            throw new FileException("Failed to compress %s".formatted(path), exception);
        }

        return this;
    }

    @Override
    public @NonNull FileManager writeFile(@NonNull final Path path, @NonNull final String content) {
        try {
            Files.writeString(path, content, StandardOpenOption.APPEND);
        } catch (final IOException exception) {
            throw new FileException("Failed to write %s to %s".formatted(content, path), exception);
        }

        return this;
    }

    @Override
    public void setDepth(final int depth) {
        this.depth = depth;
    }

    @Override
    public int getDepth() {
        return this.depth;
    }

    @Override
    public final boolean hasFile(@NonNull final Path path) {
        return this.files.containsKey(path);
    }

    @Override
    public @NonNull final FileManager refresh(final boolean save) { // save or reload all existing files
        if (this.files.isEmpty()) return this;

        final List<Path> keys = new ArrayList<>();

        for (final Map.Entry<Path, ICustomFile<?, ?, ?>> file : this.files.entrySet()) {
            final ICustomFile<?, ?, ?> value = file.getValue();

            if (value == null) continue;

            final Path path = value.getPath();

            if (!Files.exists(path)) {
                keys.add(file.getKey());

                continue;
            }

            if (save) {
                value.save(); // save the config
            } else {
                value.load(); // load the config
            }
        }

        if (!keys.isEmpty()) keys.forEach(this.files::remove);

        return this;
    }

    @Override
    public @NonNull final List<String> getFileByNames(@NonNull final String folder, @NonNull final Path path, @NonNull final String extension, final int depth, final boolean removeExtension) {
        final List<Path> files = getFilesByPath(folder.isEmpty() ? path : path.resolve(folder), List.of(extension), depth);

        final List<String> names = new ArrayList<>();

        for (final Path key : files) {
            final String fileName = key.getFileName().toString();

            if (!fileName.endsWith(extension)) continue;

            names.add(removeExtension ? fileName.replace(extension, "") : fileName);
        }

        return names;
    }

    @Override
    public @NonNull final List<Path> getFilesByPath(@NonNull final Path path, @NonNull final List<String> extensions, final int depth) {
        final List<Path> files = new ArrayList<>();

        if (Files.notExists(path) || !Files.isDirectory(path)) return new ArrayList<>();

        try {
            Files.walkFileTree(path, new HashSet<>(), Math.max(depth, 1), new SimpleFileVisitor<>() {
                @Override
                public @NonNull FileVisitResult visitFile(@NonNull final Path path, @NonNull final BasicFileAttributes attributes) {
                    final String fileName = path.getFileName().toString();

                    extensions.forEach(extension -> {
                        if (fileName.endsWith(extension)) files.add(path);
                    });

                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (final IOException exception) {
            throw new FileException("Failed to get a list of files", exception);
        }

        return files;
    }

    @Override
    public @NonNull final Map<Path, ICustomFile<?, ?, ?>> getFiles() {
        return Collections.unmodifiableMap(this.files);
    }
}