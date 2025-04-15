package com.ryderbelserion.fusion.core.managers.files.types;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.core.api.enums.FileType;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.interfaces.ICustomFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.nio.file.Path;
import java.util.function.Consumer;

public class JaluCustomFile extends ICustomFile<JaluCustomFile> {

    private final Consumer<SettingsManagerBuilder> builder;
    private final YamlFileResourceOptions options;

    private SettingsManager config;

    public JaluCustomFile(@NotNull final Path path, @NotNull Consumer<SettingsManagerBuilder> builder, @Nullable final YamlFileResourceOptions options, final boolean isStatic) {
        super(path, isStatic);

        this.builder = builder;
        this.options = options == null ? YamlFileResourceOptions.builder().indentationSize(2).build() : options;
    }

    public JaluCustomFile(@NotNull final Path path, @NotNull final Consumer<SettingsManagerBuilder> builder, final boolean isStatic) {
        this(path, builder, null, isStatic);
    }

    @Override
    public final JaluCustomFile build() {
        final SettingsManagerBuilder builder = SettingsManagerBuilder.withYamlFile(getPath(), this.options);

        this.builder.accept(builder);

        this.config = builder.create();

        return this;
    }

    @Override
    public final JaluCustomFile load() {
        if (this.config == null) {
            throw new FusionException("There was no settings manager available for " + getFileName());
        }

        this.config.reload();

        return this;
    }

    @Override
    public final JaluCustomFile save() {
        if (this.config == null) {
            throw new FusionException("There was no settings manager available for " + getFileName());
        }

        this.config.save();

        return this;
    }

    @Override
    public final boolean isLoaded() {
        return this.config != null;
    }

    @Override
    public final FileType getType() {
        return FileType.JALU;
    }

    public final SettingsManager getConfig() {
        return this.config;
    }
}