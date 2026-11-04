package com.ryderbelserion.fusion.files;

import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.exceptions.CustomException;
import com.ryderbelserion.fusion.files.interfaces.AbstractCustomFile;
import com.ryderbelserion.fusion.files.interfaces.AbstractFileManager;
import com.ryderbelserion.fusion.files.objects.FileData;
import com.ryderbelserion.fusion.files.types.YamlCustomFile;
import org.jspecify.annotations.NullMarked;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

@NullMarked
public class FileManager extends AbstractFileManager<FileManager> {

    private final Map<Path, AbstractCustomFile<?, ?, ?>> files = new ConcurrentHashMap<>();

    private final Path path;

    public FileManager(final Path path) {
        this.path = path;
    }

    @Override
    public FileManager addFolder(final Path folder, final String jarPath, final FileType fileType, final Consumer<AbstractCustomFile<?, ?, ?>> builder) {
        extractFolder(folder.getFileName().toString(), jarPath, fileType, folder.getParent());

        getFilesByPath(folder, fileType.getExtension()).forEach(entry -> addFile(entry, fileType, builder));

        return this;
    }

    @Override
    public FileManager addFile(final Path path, final String jarFolder, final FileType fileType, final Consumer<AbstractCustomFile<?, ?, ?>> builder) {
        if (this.files.containsKey(path)) {
            final AbstractCustomFile<?, ?, ?> file = this.files.get(path);

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
        final Optional<AbstractCustomFile<?, ?, ?>> variable = getFile(path);

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
    public FileManager purge() {
        final Map<Path, AbstractCustomFile<?, ?, ?>> files = new HashMap<>(this.files);

        for (final Map.Entry<Path, AbstractCustomFile<?, ?, ?>> entry : files.entrySet()) {
            removeFile(entry.getKey());
        }

        return this;
    }

    @Override
    public FileManager addFile(final Path path, final AbstractCustomFile<?, ?, ?> customFile) {
        this.files.putIfAbsent(path, customFile);

        return this;
    }

    @Override
    public FileManager reloadFile(final Path path) {
        final Optional<AbstractCustomFile<?, ?, ?>> customFile = getFile(path);

        if (customFile.isEmpty()) return this;

        customFile.get().load();

        return this;
    }

    @Override
    public FileManager saveFile(final Path path) {
        final Optional<AbstractCustomFile<?, ?, ?>> customFile = getFile(path);

        if (customFile.isEmpty()) return this;

        customFile.get().save();

        return this;
    }

    @Override
    public Optional<AbstractCustomFile<?, ?, ?>> getFile(final Path path) {
        return Optional.ofNullable(this.files.get(path));
    }

    @Override
    public FileManager extractFile(final String input, final Predicate<? super JarEntry> predicate) {
        final Path output = this.path.resolve(input);

        if (Files.exists(output)) {
            return this;
        }

        final Path parent = output.getParent();

        if (Files.notExists(parent)) {
            try {
                Files.createDirectory(parent);
            } catch (final IOException exception) {
                throw new CustomException("Failed to create %s".formatted(parent));
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
                    throw new CustomException("Failed to copy %s to %s".formatted(input, output), exception);
                }
            }, () -> {
                throw new CustomException("Failed to find %s in the jar!".formatted(input));
            });
        } catch (final IOException | URISyntaxException exception) {
            exception.printStackTrace();
        }

        return this;
    }

    @Override
    public Optional<JarEntry> getEntry(final JarFile jarFile, final Predicate<? super JarEntry> predicate) {
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
    public FileManager extractFile(final String input, final Path output) {
        if (Files.exists(output)) {
            return this;
        }

        final Path parent = output.getParent();

        if (Files.notExists(parent)) {
            try {
                Files.createDirectory(parent);
            } catch (final IOException exception) {
                throw new CustomException("Failed to create %s".formatted(parent));
            }
        }

        try (final JarFile jarFile = new JarFile(Path.of(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).toFile())) {
            getEntry(jarFile, test -> !test.getName().equalsIgnoreCase(input)).ifPresentOrElse(entry -> {
                try (final InputStream stream = jarFile.getInputStream(entry)) {
                    Files.copy(stream, output);
                } catch (final IOException exception) {
                    throw new CustomException("Failed to copy " + entry.getName() + " to " + output + ".", exception);
                }
            }, () -> {
                throw new CustomException("Failed to fetch " + input + " internally.");
            });
        } catch (final IOException | URISyntaxException exception) {
            throw new CustomException("Failed to fetch " + input + " internally.", exception);
        }

        return this;
    }

    @Override
    public FileManager extractFolder(final String folder, final String jarFolder, final FileType fileType, final Path output) {
        final Path path = output.resolve(folder);

        if (Files.exists(path)) { // do not extract if path exists.
            return this;
        }

        if (Files.notExists(path)) { // create folder
            try {
                Files.createDirectories(path);
            } catch (final Exception exception) {
                throw new CustomException("Failed to create %s".formatted(path), exception);
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
                        throw new CustomException("Failed to create %s".formatted(parent), exception);
                    }
                }

                if (Files.notExists(target)) {
                    try (final InputStream stream = jar.getInputStream(entry)) {
                        Files.copy(stream, target);
                    } catch (final IOException exception) {
                        throw new CustomException("Failed to copy %s to %s".formatted(target, parent), exception);
                    }
                }
            });
        } catch (final IOException | URISyntaxException exception) {
            throw new CustomException("Failed to extract folder %s".formatted(path), exception);
        }

        return this;
    }

    @Override
    public final boolean hasFile(final Path path) {
        return this.files.containsKey(path);
    }

    @Override
    public final FileManager refresh(final boolean save) { // save or reload all existing files
        if (this.files.isEmpty()) return this;

        final List<Path> keys = new ArrayList<>();

        for (final Map.Entry<Path, AbstractCustomFile<?, ?, ?>> file : this.files.entrySet()) {
            final AbstractCustomFile<?, ?, ?> value = file.getValue();

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
    public final Map<Path, AbstractCustomFile<?, ?, ?>> getFiles() {
        return Collections.unmodifiableMap(this.files);
    }
}