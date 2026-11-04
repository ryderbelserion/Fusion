package com.ryderbelserion.fusion.files.interfaces;

import com.ryderbelserion.fusion.config.properties.PropertyManager;
import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.exceptions.CustomException;
import com.ryderbelserion.fusion.impl.FusionConfig;
import com.ryderbelserion.fusion.impl.keys.RootKeys;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public abstract class AbstractFileManager<T> {

    protected final Path path;

    public AbstractFileManager(final Path path) {
        this.path = path;
    }

    protected FusionConfig config;
    protected PropertyManager

    public abstract List<String> getFileByNames(final String folder, final Path path, final String extension, final int depth, final boolean removeExtension);

    public List<String> getFileByNames(final String folder, final Path path, final String extension, final boolean removeExtension) {
        return getFileByNames(folder, path, extension, this.config.getConfig().getProperty(RootKeys.recursion_depth), removeExtension);
    }

    public abstract List<Path> getFilesByPath(final Path path, final List<String> extensions, final int depth);

    public List<Path> getFilesByPath(final Path path, final String extension, final int depth) {
        return getFilesByPath(path, List.of(extension), depth);
    }

    public abstract T addFolder(final Path folder, final String jarFolder, final FileType fileType, final Consumer<AbstractCustomFile<?, ?, ?>> builder);

    public T addFolder(final Path folder, final String jarFolder, final FileType fileType) {
        return addFolder(folder, jarFolder, fileType, builder -> builder.addAction(FileAction.EXTRACT_FOLDER));
    }

    public abstract T addFile(final Path path, final String jarFolder, final FileType fileType, final Consumer<AbstractCustomFile<?, ?, ?>> builder);

    public T addFile(final Path path, final String jarFolder, final FileType fileType) {
        return addFile(path, jarFolder, fileType, builder -> builder.addAction(FileAction.EXTRACT_FILE));
    }

    public T addFolder(final Path folder, final FileType fileType, final Consumer<AbstractCustomFile<?, ?, ?>> builder) {
        return addFolder(folder, "", fileType, builder);
    }

    public T addFolder(final Path folder, final FileType fileType) {
        return addFolder(folder, fileType, builder -> builder.addAction(FileAction.EXTRACT_FOLDER));
    }

    public T addFile(final Path path, final FileType fileType, final Consumer<AbstractCustomFile<?, ?, ?>> builder) {
        return addFile(path, "", fileType, builder);
    }

    public T addFile(final Path path, final FileType fileType) {
        return addFile(path, fileType, builder -> builder.addAction(FileAction.EXTRACT_FILE));
    }

    public abstract T addFile(final Path path, final AbstractCustomFile<?, ?, ?> customFile);

    public abstract T removeFile(final Path path);

    public abstract Optional<? extends AbstractCustomFile<?,?,?>> getFile(final Path input);

    public abstract T purge();

    public void init() {
        this.config = new FusionConfig(this.path);
        this.config.build();
    }

    public T extractFile(final String input, final Predicate<? super JarEntry> predicate) {
        final Path output = this.path.resolve(input);

        if (Files.exists(output)) {
            return (T) this;
        }

        final Path parent = output.getParent();

        if (Files.notExists(parent)) {
            try {
                Files.createDirectory(parent);
            } catch (final IOException exception) {
                throw new CustomException("Failed to create " + parent + ".", exception);
            }
        }

        try (final JarFile jarFile = new JarFile(Path.of(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).toFile())) {
            getEntry(jarFile, predicate).ifPresentOrElse(entry -> {
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

        return (T) this;
    }

    public T extractFile(final String input) {
        return extractFile(input, entry -> entry.getName().equalsIgnoreCase(input));
    }

    public T extractFile(final String input, final Path output) {
        if (Files.exists(output)) {
            return (T) this;
        }

        final Path parent = output.getParent();

        if (Files.notExists(parent)) {
            try {
                Files.createDirectory(parent);
            } catch (final IOException exception) {
                throw new CustomException("Failed to create " + parent + ".", exception);
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

        return (T) this;
    }

    public T extractFolder(final String folder, final String jarPath, final FileType fileType, final Path output) {
        final Path path = output.resolve(folder);

        if (Files.exists(path)) { // do not extract if path exists.
            return (T) this;
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
                    .filter(entry -> entry.getName().startsWith(jarPath.isBlank() ? folder : jarPath))
                    .filter(entry -> entry.getName().endsWith(fileType.getExtension()))
                    .collect(Collectors.toSet());

            entries.forEach(entry -> {
                final Path target = output.resolve(parseFolder(entry.getName(), jarPath));

                final Path parent = target.getParent();

                if (Files.notExists(parent)) {
                    try {
                        Files.createDirectories(parent);
                    } catch (final IOException exception) {
                        throw new CustomException("Failed to create " + parent + ".", exception);
                    }
                }

                if (Files.notExists(target)) {
                    try (final InputStream stream = jar.getInputStream(entry)) {
                        Files.copy(stream, target);
                    } catch (final IOException exception) {
                        throw new CustomException("Failed to copy " + target + " to " + parent + ".", exception);
                    }
                }
            });
        } catch (final IOException | URISyntaxException exception) {
            throw new CustomException("Failed to extract folder " + this.path, exception);
        }

        return (T) this;
    }

    public Optional<JarEntry> getEntry(final JarFile jarFile, final Predicate<? super JarEntry> predicate) {
        JarEntry target = null;

        for (final JarEntry entry : Collections.list(jarFile.entries())) {
            final String entryName = entry.getName();

            if (entryName.endsWith(".class") ||
                    entryName.startsWith("META-INF") ||
                    entryName.equalsIgnoreCase("velocity-plugin.json")
            ) continue;

            if (!predicate.test(entry)) continue;

            target = entry;

            break;
        }

        return Optional.ofNullable(target);
    }

    public String parseFolder(final String name, final String jarPath) {
        return jarPath.isBlank() ? name : name.replace("%s%s".formatted(jarPath, "/"), "");
    }
}