package com.ryderbelserion.fusion.config.properties;

import com.ryderbelserion.fusion.config.migration.MigrationService;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.exceptions.FileException;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import com.ryderbelserion.fusion.config.ConfigBuilder;
import com.ryderbelserion.fusion.config.properties.interfaces.IPropertyData;
import com.ryderbelserion.fusion.config.properties.objects.interfaces.Property;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

@NullMarked
public final class PropertyManager {

    private @Nullable final UnaryOperator<ConfigurationOptions> options;
    private @Nullable final MigrationService migrationService;
    private final YamlConfigurationLoader loader;
    private final IPropertyData propertyData;
    private final ConfigBuilder builder;
    private final FileType fileType;
    private final Path path;

    @NullUnmarked
    public PropertyManager(@NonNull final IPropertyData propertyData,
                           @Nullable final UnaryOperator<ConfigurationOptions> options,
                           @Nullable final MigrationService migrationService,
                           @NonNull final ConfigBuilder builder,
                           @NonNull final FileType fileType,
                           @NonNull final Path path
    ) {
        this.migrationService = migrationService;
        this.propertyData = propertyData;
        this.fileType = fileType;
        this.options = options;
        this.builder = builder;
        this.path = path;

        final YamlConfigurationLoader.Builder loader = YamlConfigurationLoader.builder()
                .indent(this.builder.getIndent())
                .nodeStyle(this.builder.getNodeStyle())
                .headerMode(this.builder.getHeaderMode())
                .path(getPath());

        if (this.options != null) {
            loader.defaultOptions(this.options);
        }

        this.loader = loader.build();
    }

    private @Nullable CommentedConfigurationNode configuration = null;

    public <T> T getProperty(final Property<T> property) {
        return this.propertyData.getProperty(getConfiguration(), property);
    }

    public <T> boolean hasProperty(final Property<T> property) {
        return this.propertyData.hasProperty(getConfiguration(), property);
    }

    public <T> PropertyManager setProperty(final Property<T> property, final T value) {
        this.propertyData.setProperty(getConfiguration(), property, value);

        save();

        return this;
    }

    public PropertyManager setComment(final String value, final Object... path) {
        this.propertyData.setComment(getConfiguration(), value, path);

        save();

        return this;
    }

    public Object[] getAlias(final String alias, final Object... path) {
        return this.propertyData.getAlias(alias, path);
    }

    public PropertyManager populate() {
        final CommentedConfigurationNode configuration = getConfiguration();

        this.propertyData.populate(configuration);

        if (this.migrationService != null) {
            this.migrationService.copy(configuration, this.propertyData);
        }

        save();

        return this;
    }

    public CommentedConfigurationNode getConfiguration() {
        if (this.configuration == null) {
            throw new FileException("Could not find custom file for " + this.path);
        }

        return this.configuration;
    }

    public void reload() {
        load();
    }

    public void load() {
        this.configuration = CompletableFuture.supplyAsync(() -> {
            try {
                return this.loader.load();
            } catch (final IOException exception) {
                throw new FileException("Failed to load file %s".formatted(this.path), exception);
            }
        }).join();

        populate(); // populate bean mapper
    }

    public void save() {
        if (this.configuration == null) {
            return;
        }

        CompletableFuture.runAsync(() -> {
            try {
                this.loader.save(this.configuration);
            } catch (ConfigurateException exception) {
                throw new FileException("Failed to save file %s".formatted(this.path), exception);
            }
        });
    }

    public FileType getFileType() {
        return this.fileType;
    }

    public Path getPath() {
        return this.path;
    }
}