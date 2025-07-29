package com.ryderbelserion.fusion.core.files;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.files.enums.FileType;
import com.ryderbelserion.fusion.core.files.interfaces.ICustomFile;
import com.ryderbelserion.fusion.core.files.interfaces.IFileManager;
import com.ryderbelserion.fusion.core.files.types.JsonCustomFile;
import com.ryderbelserion.fusion.core.files.types.JaluCustomFile;
import com.ryderbelserion.fusion.core.files.types.LogCustomFile;
import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileManager extends IFileManager<FileManager> {

    private final FusionCore fusion;
    private final Path dataPath;

    public FileManager(@NotNull final FusionCore fusion) {
        this.fusion = fusion;
        this.dataPath = this.fusion.getDataPath();
    }

    private final Map<Key, ICustomFile<?, ?, ?, ?>> files = new HashMap<>();

    @Override
    public @NotNull FileManager addFile(@NotNull final Key key, @NotNull final FileType fileType, @NotNull final Consumer<ICustomFile<?, ?, ?, ?>> consumer) {
        ICustomFile<?, ?, ?, ?> customFile = null;

        switch (fileType) {
            case CONFIGURATE_YAML, FUSION_YAML -> customFile = buildYamlFile(consumer::accept);
            case CONFIGURATE_GSON, FUSION_GSON -> customFile = buildJsonFile(consumer::accept);
            case JALU -> customFile = buildJaluFile(consumer::accept);
            case LOG -> customFile = buildLogFile(consumer::accept);
        }

        if (customFile == null) return this;

        customFile.setFileType(fileType);

        this.files.putIfAbsent(key, customFile);

        return this;
    }

    @Override
    public @NotNull FileManager removeFile(@NotNull final Key key) {
        final ICustomFile<?, ?, ?, ?> customFile = getFile(key);

        final FileType fileType = customFile.getFileType();

        if (fileType == FileType.FUSION_YAML || fileType == FileType.FUSION_GSON) return this; // do not allow removing these files

        this.files.remove(key);

        return this;
    }

    @Override
    public @NotNull FileManager purge() {
        final Map<Key, ICustomFile<?, ?, ?, ?>> files = new HashMap<>(this.files);

        for (final Map.Entry<Key, ICustomFile<?, ?, ?, ?>> entry : files.entrySet()) {
            removeFile(entry.getKey());
        }

        return this;
    }

    public @NotNull FileManager addFile(@NotNull final Key key, @NotNull final ICustomFile<?, ?, ?, ?> customFile) {
        this.files.putIfAbsent(key, customFile);

        return this;
    }

    @Override
    public @NotNull YamlCustomFile buildYamlFile(@NotNull final Consumer<YamlCustomFile> consumer) {
        return new YamlCustomFile(this, consumer).load();
    }

    @Override
    public @NotNull JsonCustomFile buildJsonFile(@NotNull final Consumer<JsonCustomFile> consumer) {
        return new JsonCustomFile(this, consumer).load();
    }

    @Override
    public @NotNull JaluCustomFile buildJaluFile(@NotNull final Consumer<JaluCustomFile> consumer) {
        return new JaluCustomFile(this, consumer).load();
    }

    @Override
    public @NotNull LogCustomFile buildLogFile(@NotNull final Consumer<LogCustomFile> consumer) {
        return new LogCustomFile(this, consumer).load();
    }

    @Override
    public @NotNull FileManager reloadFile(@NotNull final Key key) {
        final ICustomFile<?, ?, ?, ?> customFile = this.files.get(key);

        if (customFile == null) return this;

        customFile.load();

        return this;
    }

    @Override
    public @NotNull ICustomFile<?, ?, ?, ?> getFile(@NotNull final Key key) {
        return this.files.getOrDefault(key, null);
    }

    @Override
    public @NotNull FileManager extractFolder(@NotNull final String folder, @NotNull final Path output) {
        final Path path = output.resolve(folder);

        if (Files.notExists(path)) { // create folder
            try {
                Files.createDirectories(path);
            } catch (final Exception exception) {
                exception.printStackTrace();
            }
        }

        try (final JarFile jarFile = new JarFile(Path.of(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).toFile())) {
            final Set<JarEntry> entries = jarFile.stream().filter(entry -> !entry.getName().endsWith(".class"))
                    .filter(entry -> !entry.getName().startsWith("META-INF"))
                    .filter(entry -> !entry.isDirectory())
                    .filter(entry -> entry.getName().startsWith(folder))
                    .collect(Collectors.toSet());

            entries.forEach(entry -> {
                final Path target = output.resolve(entry.getName());
                final Path parent = target.getParent();

                if (!Files.exists(parent)) {
                    try {
                        Files.createDirectories(parent);
                    } catch (final IOException exception) {
                        exception.printStackTrace();
                    }
                }

                if (Files.notExists(target)) {
                    try (final InputStream stream = jarFile.getInputStream(entry)) {
                        Files.copy(stream, target);
                    } catch (final IOException exception) {
                        exception.printStackTrace();
                    }
                }
            });
        } catch (final IOException | URISyntaxException exception) {
            exception.printStackTrace();
        }

        return this;
    }

    @Override
    public @NotNull FileManager extractFile(@NotNull final Path path) {
        final String fileName = path.getFileName().toString();

        try (final JarFile jarFile = new JarFile(Path.of(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).toFile())) {
            final Set<JarEntry> entries = jarFile.stream().filter(entry -> !entry.getName().endsWith(".class"))
                    .filter(entry -> !entry.getName().startsWith("META-INF"))
                    .filter(entry -> !entry.isDirectory())
                    .filter(entry -> entry.getName().equalsIgnoreCase(fileName))
                    .collect(Collectors.toSet());

            entries.forEach(entry -> {
                if (Files.notExists(path)) {
                    try (final InputStream stream = jarFile.getInputStream(entry)) {
                        Files.copy(stream, path);
                    } catch (final IOException exception) {
                        exception.printStackTrace();
                    }
                }
            });
        } catch (final IOException | URISyntaxException exception) {
            exception.printStackTrace();
        }

        return this;
    }

    @Override
    public @NotNull FileManager compressFolder(@NotNull final Path path, @NotNull final String content) {
        if (!Files.exists(path)) return this;
        if (!Files.isDirectory(path)) return this;

        final Path target = this.dataPath.resolve(asString(path, content));

        try (final ZipOutputStream output= new ZipOutputStream(Files.newOutputStream(target)); final Stream<Path> values = Files.walk(path)) {
            final List<Path> entries = values.filter(key -> !Files.isDirectory(key)).toList();

            for (final Path entry : entries) {
                if (Files.size(entry) <= 0L) continue;

                final ZipEntry zipEntry = new ZipEntry(entry.toString());

                output.putNextEntry(zipEntry);

                Files.copy(entry, output);

                output.closeEntry();
            }
        } catch (final IOException exception) {
            exception.printStackTrace();
        }

        return this;
    }

    @Override
    public @NotNull FileManager compressFile(@NotNull final Path path, @Nullable final Path folder, @NotNull final String content) {
        if (!Files.exists(path)) return this;

        long size = 0L;

        try {
            size = Files.size(path);
        } catch (final Exception exception) {
            exception.printStackTrace();
        }

        if (size <= 0L) return this;

        final String builder = asString(path, content);

        final Path target = folder == null ? this.dataPath : folder.resolve(builder);

        try (final ZipOutputStream output = new ZipOutputStream(Files.newOutputStream(target))) {
            final ZipEntry entry = new ZipEntry(path.getFileName().toString());

            output.putNextEntry(entry);

            Files.copy(path, output);

            output.closeEntry();
        } catch (final Exception exception) {
            exception.printStackTrace();
        }

        return this;
    }

    @Override
    public @NotNull FileManager writeFile(@NotNull final Path path, @NotNull final String content) {
        try {
            Files.writeString(path, content, StandardOpenOption.APPEND);
        } catch (final IOException exception) {
            throw new FusionException(String.format("Failed to write %s to %s", content, path), exception);
        }

        return this;
    }

    @Override
    public final int getFileCount(@NotNull final Path path, @NotNull final String extension) {
        return this.fusion.getFiles(path, extension, 1).size();
    }

    private String asString(@NotNull final Path path, @NotNull final String content) {
        final StringBuilder builder = new StringBuilder();

        builder.append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        if (!content.isEmpty()) builder.append(content);

        final int fileCount = this.getFileCount(path, ".gz");

        builder.append("-").append(fileCount).append(".gz");

        return builder.toString();
    }
}