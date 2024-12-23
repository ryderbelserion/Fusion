package com.ryderbelserion.paper.files;

import com.ryderbelserion.paper.FusionApi;
import com.ryderbelserion.core.api.enums.FileType;
import com.ryderbelserion.core.api.exception.FusionException;
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

    public final CustomFile load() {
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

    public final CustomFile save() {
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

    public final @Nullable YamlConfiguration getConfiguration() {
        return this.configuration;
    }

    public final boolean isConfigurationLoaded() {
        return getConfiguration() != null;
    }

    public boolean isDynamic() {
        return this.isDynamic;
    }

    public CustomFile getInstance() {
        return this;
    }

    public String getEffectiveName() {
        return this.effectiveName;
    }

    public String getFileName() {
        return this.file.getName();
    }

    public boolean isFileLoaded() {
        return this.file.exists();
    }

    public FileType getFileType() {
        return this.fileType;
    }

    public File getFile() {
        return this.file;
    }
}