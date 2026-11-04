package com.ryderbelserion.fusion.files.interfaces;

import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.objects.FileData;
import org.jspecify.annotations.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractCustomFile<T, L, N> {

    protected final List<FileAction> actions = new ArrayList<>();

    protected @Nullable N configuration;
    protected @Nullable L loader;

    protected final FileManager fileManager;
    protected final boolean isDirectory;
    protected final FileData fileData;
    protected final String jarPath;
    protected Path parent;
    protected Path path;

    public AbstractCustomFile(final FileManager fileManager, final FileData fileData) {
        this.fileManager = fileManager;

        this.fileData = fileData;

        this.path = this.fileData.path();
        this.parent = this.path.getParent();
        this.isDirectory = Files.isDirectory(this.path);
        this.jarPath = this.fileData.jarPath();
    }

    public abstract void write(final T value, final Object... args);

    public abstract N loadConfig() throws IOException;

    public abstract void saveConfig() throws IOException;

    public final boolean hasAction(final FileAction action) {
        return this.actions.contains(action);
    }

    public void addAction(final FileAction action) {
        this.actions.add(action);
    }

    public N getConfig() {
        if (this.configuration == null) {
            throw new RuntimeException("No configuration found.");
        }

        return this.configuration;
    }

    public void withPath(final Path path) {
        try {
            if (Files.isSameFile(this.path, path)) {
                return;
            }

            Files.move(this.path, path);

            this.path = path;
            this.parent = path.getParent();
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    public AbstractCustomFile<T, L, N> load() {
        if (this.isDirectory) return this;

        final String name = this.path.getFileName().toString();

        if (!this.actions.contains(FileAction.ALREADY_EXTRACTED)) {
            if (this.actions.contains(FileAction.EXTRACT_FROM_FOLDER)) {
                this.fileManager.extractFile("%s/%s".formatted(this.parent.getFileName().toString(), name), this.path);
            }

            if (this.actions.contains(FileAction.EXTRACT_FOLDER)) {
                this.fileManager.extractFolder(name, this.jarPath, this.fileData.fileType(), this.parent);
            }

            if (this.actions.contains(FileAction.EXTRACT_FILE)) {
                this.fileManager.extractFile(name);
            }
        }

        if (this.actions.contains(FileAction.CREATE_FILE)) {
            try {
                Files.createFile(this.path);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        this.configuration = CompletableFuture.supplyAsync(() -> {
            try {
                return loadConfig();
            } catch (final IOException exception) {
                throw new RuntimeException("Failed to load file %s".formatted(this.path), exception);
            }
        }).join();

        this.actions.remove(FileAction.RELOAD_FILE);

        return this;
    }

    public void save() {
        if (this.isDirectory) {
            return;
        }

        if (this.configuration == null) {
            return;
        }

        CompletableFuture.runAsync(() -> {
            try {
                saveConfig();
            } catch (final IOException exception) {
                throw new RuntimeException("Failed to save file %s".formatted(this.path), exception);
            }
        });
    }

    public String getPrettyName() {
        return this.fileData.getPrettyName();
    }

    public String getName() {
        return this.fileData.getFileName();
    }

    public boolean isLoaded() {
        return Files.exists(this.path);
    }

    public Path getPath() {
        return this.path;
    }
}