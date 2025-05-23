package com.ryderbelserion.fusion.paper.files;

import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.files.FileType;
import com.ryderbelserion.fusion.core.utils.FileUtils;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.paper.FusionPlugin;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class LegacyFileManager {

    private final FusionCore api = FusionCore.Provider.get();

    private final Plugin plugin = FusionPlugin.getPlugin();

    private final ComponentLogger logger = this.plugin.getComponentLogger();
    private final File dataFolder = this.plugin.getDataFolder();
    private final boolean isVerbose = this.api.isVerbose();

    private final Map<String, LegacyCustomFile> files = new HashMap<>();

    private final Map<String, FileType> folders = new HashMap<>();

    public LegacyFileManager() {}

    public LegacyFileManager addFolder(@NotNull final String folder, @NotNull final FileType fileType) {
        if (folder.isBlank()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot add the folder as the folder is empty.");
            }

            return this;
        }

        if (!this.folders.containsKey(folder)) {
            this.folders.put(folder, fileType);
        }

        final File directory = new File(this.dataFolder, folder);

        if (!directory.exists()) {
            directory.mkdirs();

            extracts(String.format("/%s/", directory.getName()), directory.toPath(), false);
        }

        final File[] contents = directory.listFiles();

        if (contents == null) return this;

        final String extension = fileType.getExtension();

        for (final File file : contents) {
            if (file.isDirectory()) {
                final String[] files = file.list();

                if (files == null) continue;

                for (final String fileName : files) {
                    if (!fileName.endsWith(extension)) continue; // just in case people are weird

                    addFile(fileName, folder + File.separator + file.getName(), true, fileType);
                }

                continue;
            }

            final String fileName = file.getName();

            if (!fileName.endsWith(extension)) continue; // just in case people are weird

            addFile(fileName, folder, true, fileType);
        }

        return this;
    }

    public LegacyFileManager addFile(@NotNull final LegacyCustomFile customFile) {
        this.files.put(customFile.getEffectiveName(), customFile);
        
        return this;
    }

    public LegacyFileManager addFile(@NotNull final String fileName) {
        FileType type = FileType.NONE;

        if (fileName.endsWith(".yml")) {
            type = FileType.YAML;
        } else if (fileName.endsWith(".nbt")) {
            type = FileType.NBT;
        }

        return addFile(fileName, null, false, type);
    }

    public LegacyFileManager addFile(@NotNull final String fileName, @NotNull final FileType fileType) {
        return addFile(fileName, null, false, fileType);
    }

    public LegacyFileManager addFile(@NotNull final String fileName, @Nullable final String folder, final boolean isDynamic, @NotNull final FileType fileType) {
        if (fileName.isBlank()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot add the file as the file is null or empty.");
            }

            return this;
        }

        final String extension = fileType.getExtension();

        final String strippedName = strip(fileName, extension);

        final String resourcePath = folder != null ? folder + File.separator + fileName : fileName;

        final File file = new File(this.dataFolder, resourcePath);

        if (!file.exists()) {
            if (this.isVerbose) {
                this.logger.warn("Successfully extracted file {} to {}", fileName, file.getPath());
            }

            this.plugin.saveResource(resourcePath, false);
        }

        switch (fileType) {
            case NBT -> {
                if (this.files.containsKey(strippedName)) {
                    throw new FusionException("The file '" + strippedName + "' already exists.");
                }

                this.files.put(strippedName, new LegacyCustomFile(fileType, file, isDynamic));
            }

            case YAML -> {
                if (this.files.containsKey(strippedName)) {
                    this.files.get(strippedName).load();

                    return this;
                }

                this.files.put(strippedName, new LegacyCustomFile(fileType, file, isDynamic).load());
            }

            //case JSON -> throw new FusionException("The file type with extension " + extension + " is not currently supported.");

            case NONE -> {} // do nothing
        }

        return this;
    }

    public LegacyFileManager saveFile(@NotNull final String fileName) {
        if (fileName.isBlank()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot save the file as the file is null or empty.");
            }

            return this;
        }

        final String extension = FileType.YAML.getExtension();

        final String strippedName = strip(fileName, extension);

        if (!this.files.containsKey(strippedName)) {
            if (this.isVerbose) {
                this.logger.warn("Cannot save the file as the file does not exist.");
            }

            return this;
        }

        this.files.get(strippedName).save();

        return this;
    }

    public LegacyFileManager removeFile(final LegacyCustomFile customFile, final boolean purge) {
        return removeFile(customFile.getFileName(), customFile.getFileType(), purge);
    }

    public LegacyFileManager removeFile(@NotNull final String fileName, @NotNull final FileType fileType, final boolean purge) {
        if (fileName.isBlank()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot remove the file as the file is null or empty.");
            }

            return this;
        }

        final String strippedName = strip(fileName, fileType.getExtension());

        switch (fileType) {
            case YAML -> {
                if (!this.files.containsKey(strippedName)) return this;

                final LegacyCustomFile customFile = this.files.remove(fileName);

                if (purge) {
                    customFile.delete();

                    return this;
                }

                customFile.save();
            }

            case NBT -> {
                if (!this.files.containsKey(strippedName)) return this;

                final LegacyCustomFile customFile = this.files.remove(fileName);

                if (purge) {
                    customFile.delete();

                    return this;
                }
            }
        }

        return this;
    }

    public LegacyFileManager reloadFiles() {
        final List<String> forRemoval = new ArrayList<>();

        this.files.forEach((name, file) -> {
            if (file.getFile().exists()) {
                if (file.getFileType() == FileType.YAML) file.load();
            } else {
                forRemoval.add(name);
            }
        });

        forRemoval.forEach(this.files::remove);

        if (this.isVerbose && !forRemoval.isEmpty()) {
            this.logger.info("{} file(s) were removed from cache, because they did not exist.", forRemoval.size());
        }

        return this;
    }

    public LegacyFileManager init() {
        this.dataFolder.mkdirs();

        this.folders.forEach(this::addFolder);

        return this;
    }

    public LegacyFileManager purge() {
        this.folders.clear();
        this.files.clear();

        return this;
    }

    public @Nullable LegacyCustomFile getFile(final String fileName, final FileType fileType) {
        return this.files.getOrDefault(strip(fileName, fileType.getExtension()), null);
    }


    public String strip(final String fileName, final String extension) {
        return fileName.replace(extension, "");
    }

    public Map<String, LegacyCustomFile> getFiles() {
        return Collections.unmodifiableMap(this.files);
    }

    public void extracts(@NotNull String input, @Nullable final Path output, final boolean replaceExisting) {
        if (output == null || input.isEmpty()) return;

        try (JarFile jarFile = new JarFile(Path.of(FileUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toFile())) {
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

    private InputStream getResource(@NotNull final String path) {
        try {
            final URL url = FileUtils.class.getClassLoader().getResource(path);

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