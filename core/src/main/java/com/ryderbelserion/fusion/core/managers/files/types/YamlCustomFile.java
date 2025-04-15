package com.ryderbelserion.fusion.core.managers.files.types;

import com.ryderbelserion.fusion.core.api.enums.FileType;
import com.ryderbelserion.fusion.core.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.interfaces.ICustomFile;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class YamlCustomFile extends ICustomFile<YamlCustomFile> {

    private CommentedConfigurationNode configurationNode;
    private final YamlConfigurationLoader loader;

    public YamlCustomFile(@NotNull final Path path, final boolean isDynamic, final UnaryOperator<ConfigurationOptions> options) {
        super(path, isDynamic);

        final YamlConfigurationLoader.Builder builder = YamlConfigurationLoader.builder();

        builder.defaultOptions(options);

        this.loader = builder.indent(2).path(path).build();
    }

    public YamlCustomFile(@NotNull final Path path, final boolean isDynamic) {
        this(path, isDynamic, (option -> option));
    }

    @Override
    public final YamlCustomFile load() {
        if (isDirectory()) {
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
    public final YamlCustomFile save() {
        if (isDirectory()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot save configuration, as {} is a directory.", getFileName());
            }

            return this;
        }

        if (this.configurationNode == null) {
            if (this.isVerbose) {
                this.logger.error("Configuration is null, cannot save {}!", getFileName());
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
    public final CommentedConfigurationNode getConfigurationNode() {
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