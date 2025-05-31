package com.ryderbelserion.fusion.paper.files;

import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
import com.ryderbelserion.fusion.core.files.FileType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.util.concurrent.CompletableFuture;

public final class LegacyCustomFile {

    private final FusionCore fusion = FusionCore.Provider.get();

    private final ILogger logger = this.fusion.getLogger();

    private final String effectiveName;
    private final FileType fileType;
    private final boolean isDynamic;
    private final File file;

    public LegacyCustomFile(@NotNull final FileType fileType, @NotNull final File file, final boolean isDynamic) {
        this.effectiveName = file.getName().replace(".yml", "");
        this.isDynamic = isDynamic;
        this.fileType = fileType;
        this.file = file;
    }

    private YamlConfiguration configuration;

    public @NotNull LegacyCustomFile load() {
        if (this.fileType != FileType.YAML) {
            throw new FusionException("Only yaml files are supported by the load function.");
        }

        if (getFile().isDirectory()) {
            this.logger.warn("Cannot load configuration, as {} is a directory.", getFileName());

            return this;
        }

        try {
            this.configuration = CompletableFuture.supplyAsync(() -> YamlConfiguration.loadConfiguration(this.file)).join();
        } catch (final Exception exception) {
            this.logger.warn("Cannot load configuration file: {}", getFileName(), exception);
        }

        return this;
    }

    public @NotNull LegacyCustomFile save() {
        if (this.fileType != FileType.YAML) {
            throw new FusionException("Only yaml files are supported by the save function.");
        }

        if (getFile().isDirectory()) {
            this.logger.warn("Cannot save configuration, as {} is a directory.", getFileName());

            return this;
        }

        if (this.configuration == null) {
            this.logger.error("Configuration is null, cannot save {}!", getFileName());

            return this;
        }

        CompletableFuture.runAsync(() -> {
            try {
                this.configuration.save(this.file);
            } catch (Exception exception) {
                throw new FusionException("Cannot save configuration file: " + getFileName(), exception);
            }
        });

        return this;
    }

    public void delete() {
        final File file = getFile();

        if (file != null && file.exists() && file.delete()) {
            this.logger.warn("Successfully deleted {}", getFileName());
        }
    }

    public @Nullable YamlConfiguration getConfiguration() {
        if (this.fileType != FileType.YAML) {
            throw new FusionException("Only yaml files are supported by the getConfiguration function.");
        }

        return this.configuration;
    }

    public boolean isConfigurationLoaded() {
        if (this.fileType != FileType.YAML) {
            throw new FusionException("Only yaml files are supported by the isConfigurationLoaded function.");
        }

        return getConfiguration() != null;
    }

    public @NotNull String getEffectiveName() {
        return this.effectiveName;
    }

    public @NotNull LegacyCustomFile getInstance() {
        return this;
    }

    public @NotNull FileType getFileType() {
        return this.fileType;
    }

    public @NotNull String getFileName() {
        return this.file.getName();
    }

    public boolean isDynamic() {
        return this.isDynamic;
    }

    public boolean exists() {
        return this.file.exists();
    }

    public @NotNull File getFile() {
        return this.file;
    }
}