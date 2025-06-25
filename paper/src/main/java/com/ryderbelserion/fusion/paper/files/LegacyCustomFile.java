package com.ryderbelserion.fusion.paper.files;

import com.ryderbelserion.fusion.common.api.FusionCommon;
import com.ryderbelserion.fusion.common.FusionProvider;
import com.ryderbelserion.fusion.common.api.enums.FileType;
import com.ryderbelserion.fusion.common.api.exceptions.FusionException;
import com.ryderbelserion.fusion.common.api.interfaces.ILogger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.util.concurrent.CompletableFuture;

public class LegacyCustomFile {

    private final FusionCommon api = FusionProvider.get();

    private final ILogger logger = this.api.getLogger();

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

    public @NotNull final LegacyCustomFile load() {
        if (this.fileType != FileType.YAML) {
            this.logger.error("Only yaml files are supported by the load function.");

            return this;
        }

        if (getFile().isDirectory()) {
            this.logger.warn("Cannot load configuration, as {} is a directory.", getFileName());

            return this;
        }

        try {
            this.configuration = CompletableFuture.supplyAsync(() -> YamlConfiguration.loadConfiguration(this.file)).join();
        } catch (final Exception exception) {
            throw new FusionException(String.format("Cannot load configuration file %s!", getFileName()), exception);
        }

        return this;
    }

    public @NotNull final LegacyCustomFile save() {
        if (this.fileType != FileType.YAML) {
            this.logger.error("Only yaml files are supported by the save function.");

            return this;
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
            } catch (final Exception exception) {
                throw new FusionException(String.format("Cannot save configuration file %s!", getFileName()), exception);
            }
        });

        return this;
    }

    public void delete() {
        final File file = getFile();

        if (file.exists() && file.delete()) {
            this.logger.warn("Successfully deleted {}!", getFileName());
        }
    }

    public @Nullable final YamlConfiguration getConfiguration() {
        if (this.fileType != FileType.YAML) {
            throw new FusionException("Only yaml files are supported by the getConfiguration function.");
        }

        return this.configuration;
    }

    public final boolean isConfigurationLoaded() {
        if (this.fileType != FileType.YAML) {
            throw new FusionException("Only yaml files are supported by the isConfigurationLoaded function.");
        }

        return getConfiguration() != null;
    }

    public @NotNull final String getEffectiveName() {
        return this.effectiveName;
    }

    public @NotNull final LegacyCustomFile getInstance() {
        return this;
    }

    public @NotNull final FileType getFileType() {
        return this.fileType;
    }

    public @NotNull final String getFileName() {
        return this.file.getName();
    }

    public final boolean isDynamic() {
        return this.isDynamic;
    }

    public final boolean exists() {
        return this.file.exists();
    }

    public @NotNull final File getFile() {
        return this.file;
    }
}