package com.ryderbelserion.fusion.files;

import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.exceptions.CustomException;
import com.ryderbelserion.fusion.files.interfaces.AbstractCustomFile;
import com.ryderbelserion.fusion.files.interfaces.AbstractFileManager;
import com.ryderbelserion.fusion.files.objects.FileData;
import com.ryderbelserion.fusion.files.types.YamlCustomFile;
import org.jspecify.annotations.NonNull;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class FileManager extends AbstractFileManager<FileManager> {

    private final Map<Path, AbstractCustomFile> files = new ConcurrentHashMap<>();

    public FileManager(final Path path) {
        super(path);
    }

    @Override
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

    @Override
    public final List<Path> getFilesByPath(final Path path, final List<String> extensions, final int depth) {
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
            throw new CustomException("Failed to get a list of files.", exception);
        }

        return files;
    }

    @Override
    public FileManager addFolder(final Path folder, final String jarPath, final FileType fileType, final Consumer<AbstractCustomFile<?, ?, ?>> builder) {
        extractFolder(folder.getFileName().toString(), jarPath, fileType, path.getParent());

        getFilesByPath(folder, fileType.getExtension(), 1).forEach(entry -> addFile(entry, fileType));

        return this;
    }

    @Override
    public FileManager addFile(final Path path, final AbstractCustomFile<?, ?, ?> customFile) {
        if (this.files.containsKey(path)) {
            this.files.get(path).load();

            return this;
        }

        this.files.putIfAbsent(path, customFile.load());

        return this;
    }

    @Override
    public FileManager addFile(final Path path, final String jarFolder, final FileType fileType, final Consumer<AbstractCustomFile<?, ?, ?>> builder) {
        if (this.files.containsKey(path)) {
            final AbstractCustomFile file = this.files.get(path);

            builder.accept(file);

            if (file.hasAction(FileAction.RELOAD_FILE)) {
                file.load();
            }

            return this;
        }

        AbstractCustomFile<?, ?, ?> customFile = null;

        switch (fileType) {
            case YAML -> customFile = new YamlCustomFile(this, new FileData(path, fileType));
        }

        if (customFile == null) {
            return this;
        }

        builder.accept(customFile);

        this.files.putIfAbsent(path, customFile.load());

        return this;
    }

    @Override
    public FileManager removeFile(final Path path) {
        final Optional<AbstractCustomFile<?, ?, ?>> customFile = getFile(path);

        if (customFile.isEmpty()) {
            return this;
        }

        this.files.remove(path);

        return this;
    }

    @Override
    public Optional<AbstractCustomFile<?, ?, ?>> getFile(final Path input) {
        return Optional.ofNullable(this.files.get(input));
    }

    @Override
    public FileManager purge() {
        this.files.clear();

        return this;
    }
}