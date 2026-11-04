package com.ryderbelserion.fusion.files.interfaces;

import com.ryderbelserion.fusion.config.properties.PropertyManager;
import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.exceptions.CustomException;
import com.ryderbelserion.fusion.files.types.YamlCustomFile;
import com.ryderbelserion.fusion.impl.FusionConfig;
import com.ryderbelserion.fusion.impl.keys.RootKeys;
import com.ryderbelserion.fusion.impl.objects.ConfigProvider;
import org.jspecify.annotations.NullMarked;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@NullMarked
public abstract class AbstractFileManager<I> {

    protected final FusionConfig provider = ConfigProvider.api();

    protected final PropertyManager config = this.provider.getConfig();

    public abstract I addFolder(final Path folder, final String jarFolder, final FileType fileType, final Consumer<AbstractCustomFile<?, ?, ?>> consumer);

    public I addFolder(final Path folder, final String jarFolder, final FileType fileType) {
        return addFolder(folder, jarFolder, fileType, consumer -> consumer.addAction(FileAction.EXTRACT_FOLDER));
    }

    public abstract I addFile(final Path path, final String jarFolder, final FileType fileType, final Consumer<AbstractCustomFile<?, ?, ?>> consumer);

    public I addFile(final Path path, final String jarFolder, final FileType fileType) {
        return addFile(path, jarFolder, fileType, consumer -> consumer.addAction(FileAction.EXTRACT_FILE));
    }

    public I addFolder(final Path folder, final FileType fileType, final Consumer<AbstractCustomFile<?, ?, ?>> consumer) {
        return addFolder(folder, "", fileType, consumer);
    }

    public I addFolder(final Path folder, final FileType fileType) {
        return addFolder(folder, fileType, consumer -> consumer.addAction(FileAction.EXTRACT_FOLDER));
    }

    public I addFile(final Path path, final FileType fileType, final Consumer<AbstractCustomFile<?, ?, ?>> consumer) {
        return addFile(path, "", fileType, consumer);
    }

    public I addFile(final Path path, final FileType fileType) {
        return addFile(path, fileType, consumer -> consumer.addAction(FileAction.EXTRACT_FILE));
    }

    public abstract I addFile(final Path path, final AbstractCustomFile<?, ?, ?> customFile);

    public abstract I removeFile(final Path path);

    public abstract I reloadFile(final Path path);

    public abstract I saveFile(final Path path);

    public abstract I purge();

    public abstract I refresh(final boolean save);

    public abstract boolean hasFile(final Path path);

    public abstract Optional<AbstractCustomFile<?, ?, ?>> getFile(final Path path);

    public abstract Map<Path, AbstractCustomFile<?, ?, ?>> getFiles();

    public Optional<YamlCustomFile> getYamlFile(final Path path) {
        return getFile(path).map(YamlCustomFile.class::cast);
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

    public final List<String> getFileByNames(final String folder, final Path path, final String extension, final int depth, final boolean removeExtension) {
        final List<Path> files = getFilesByPath(folder.isEmpty() ? path : path.resolve(folder), List.of(extension), depth);

        final List<String> names = new ArrayList<>();

        for (final Path key : files) {
            final String fileName = key.getFileName().toString();

            if (!fileName.endsWith(extension)) continue;

            names.add(removeExtension ? fileName.replace(extension, "") : fileName);
        }

        return names;
    }

    public final List<Path> getFilesByPath(final Path path, final List<String> extensions, final int depth) {
        final List<Path> files = new ArrayList<>();

        if (Files.notExists(path) || !Files.isDirectory(path)) return new ArrayList<>();

        try {
            Files.walkFileTree(path, new HashSet<>(), Math.max(depth, 1), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(final Path path, final BasicFileAttributes attributes) {
                    final String fileName = path.getFileName().toString();

                    extensions.forEach(extension -> {
                        if (fileName.endsWith(extension)) files.add(path);
                    });

                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (final IOException exception) {
            throw new CustomException("Failed to get a list of files.", exception);
        }

        return files;
    }

    public List<Path> getFilesByPath(final Path path, final String extension) {
        return getFilesByPath(path, List.of(extension), this.config.getProperty(RootKeys.recursion_depth));
    }
}