package com.ryderbelserion.paper.files;

import com.ryderbelserion.FusionApi;
import com.ryderbelserion.api.enums.FileType;
import com.ryderbelserion.api.exception.FusionException;
import com.ryderbelserion.paper.Fusion;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.util.concurrent.CompletableFuture;

public final class CustomFile {

    private final Fusion api = FusionApi.get().getFusion();
    private final ComponentLogger logger = this.api.getLogger();
    private final boolean isVerbose = this.api.isVerbose();

    private final String effectiveName;
    private final FileType fileType;
    private final boolean isDynamic;
    private final File file;

    public CustomFile(final FileType fileType, final File file, final boolean isDynamic) {
        this.effectiveName = file.getName().replace(".yml", "");
        this.isDynamic = isDynamic;
        this.fileType = fileType;
        this.file = file;
    }

    private YamlConfiguration configuration;

    public CustomFile load() {
        if (this.fileType != FileType.YAML) {
            throw new FusionException("Only yaml files are supported by the load function.");
        }

        if (getFile().isDirectory()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot load configuration, as {} is a directory.", getFileName());
            }

            return this;
        }

        try {
            this.configuration = CompletableFuture.supplyAsync(() -> YamlConfiguration.loadConfiguration(this.file)).join();
        } catch (Exception exception) {
            if (this.isVerbose) {
                this.logger.warn("Cannot load configuration file: {}", getFileName(), exception);
            }
        }

        return this;
    }

    public CustomFile save() {
        if (this.fileType != FileType.YAML) {
            throw new FusionException("Only yaml files are supported by the save function.");
        }

        if (getFile().isDirectory()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot save configuration, as {} is a directory.", getFileName());
            }

            return this;
        }

        if (this.configuration == null) {
            if (this.isVerbose) {
                this.logger.error("Configuration is null, cannot save {}!", getFileName());
            }

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
            if (this.isVerbose) {
                this.logger.warn("Successfully deleted {}", getFileName());
            }
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

    public String getEffectiveName() {
        return this.effectiveName;
    }

    public CustomFile getInstance() {
        return this;
    }

    public FileType getFileType() {
        return this.fileType;
    }

    public String getFileName() {
        return this.file.getName();
    }

    public boolean isDynamic() {
        return this.isDynamic;
    }

    public boolean exists() {
        return this.file.exists();
    }

    public File getFile() {
        return this.file;
    }
}