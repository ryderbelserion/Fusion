package com.ryderbelserion.fusion.paper.files.v2;

import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.api.enums.FileAction;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CustomFile {

    private final FusionPaper fusion = (FusionPaper) FusionProvider.get();

    private YamlConfiguration configuration;
    private final String effectiveName;
    private final boolean isStatic;
    private final String name;
    private final Path path;

    public CustomFile(@NotNull final Path path, final List<FileAction> actions) {
        final String name = path.getFileName().toString();

        this.effectiveName = name.replace(".yml", "");
        this.isStatic = actions.contains(FileAction.STATIC_FILE);
        this.name = name;
        this.path = path;
    }

    public @NotNull final CustomFile load() {
        if (this.configuration == null || Files.isDirectory(this.path)) {
            this.fusion.log("info", "Cannot load {} as configuration is null, or the path is a directory.", getFileName());

            return this;
        }

        try {
            this.configuration = CompletableFuture.supplyAsync(() -> YamlConfiguration.loadConfiguration(this.path.toFile())).join();
        } catch (final Exception exception) {
            throw new FusionException(String.format("Cannot load configuration file %s!", getFileName()), exception);
        }

        return this;
    }

    public @NotNull final CustomFile save() {
        if (this.configuration == null || Files.isDirectory(this.path)) {
            this.fusion.log("info", "Cannot save {} as configuration is null, or the path is a directory.", getFileName());

            return this;
        }

        CompletableFuture.runAsync(() -> {
            try {
                this.configuration.save(this.path.toFile());
            } catch (final Exception exception) {
                throw new FusionException(String.format("Cannot save configuration file %s!", getFileName()), exception);
            }
        });

        return this;
    }

    public void delete() {
        try {
            Files.deleteIfExists(this.path);

            this.fusion.log("info", "Successfully deleted {}!", getFileName());
        } catch (final IOException exception) {
            this.fusion.log("error", "Failed to delete {}!", getFileName());
        }
    }

    public @NotNull final YamlConfiguration getConfiguration() {
        return this.configuration;
    }

    public @NotNull final String getEffectiveName() {
        return this.effectiveName;
    }

    public @NotNull final String getFileName() {
        return this.name;
    }

    public @NotNull final Path getPath() {
        return this.path;
    }

    public final boolean isStatic() {
        return this.isStatic;
    }
}