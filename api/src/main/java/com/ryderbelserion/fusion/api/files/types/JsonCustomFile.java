package com.ryderbelserion.fusion.api.files.types;

import com.ryderbelserion.fusion.api.enums.FileType;
import com.ryderbelserion.fusion.api.exceptions.FusionException;
import com.ryderbelserion.fusion.api.files.CustomFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.jackson.JacksonConfigurationLoader;
import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class JsonCustomFile extends CustomFile<JsonCustomFile> {

    private BasicConfigurationNode configurationNode;
    private final JacksonConfigurationLoader loader;

    public JsonCustomFile(@NotNull final File file, final boolean isDynamic, @Nullable final UnaryOperator<ConfigurationOptions> options) {
        super(file, isDynamic);

        final JacksonConfigurationLoader.Builder builder = JacksonConfigurationLoader.builder();

        if (options != null) {
            builder.defaultOptions(options);
        }

        this.loader = builder.indent(2).file(file).build();
    }

    public JsonCustomFile(@NotNull final File file, final boolean isDynamic) {
        this(file, isDynamic, null);
    }

    @Override
    public final JsonCustomFile load() {
        if (getFile().isDirectory()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot load configuration, as {} is a directory.", getFileName());
            }

            return this;
        }

        this.configurationNode = CompletableFuture.supplyAsync(() -> {
            try {
                return this.loader.load();
            } catch (ConfigurateException exception) {
                throw new FusionException("Failed to load configuration file: " + getFileName(), exception);
            }
        }).join();

        return this;
    }

    @Override
    public final JsonCustomFile save() {
        if (getFile().isDirectory()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot save configuration, as {} is a directory.", getFileName());
            }

            return this;
        }

        if (this.configurationNode == null) {
            if (this.isVerbose) {
                this.logger.severe("Configuration is null, cannot save {}!", getFileName());
            }

            return this;
        }

        CompletableFuture.runAsync(() -> {
            try {
                this.loader.save(this.configurationNode);
            } catch (ConfigurateException exception) {
                throw new FusionException("Failed to save configuration file: " + getFileName(), exception);
            }
        });

        return this;
    }

    @Override
    public final BasicConfigurationNode getBasicConfigurationNode() {
        return this.configurationNode;
    }

    @Override
    public final boolean isLoaded() {
        return this.configurationNode != null;
    }

    @Override
    public final FileType getType() {
        return FileType.YAML;
    }
}