package com.ryderbelserion.fusion.api.files.types;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.migration.MigrationService;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.api.enums.FileType;
import com.ryderbelserion.fusion.api.exceptions.FusionException;
import com.ryderbelserion.fusion.api.files.CustomFile;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.nio.file.Path;

public class JaluCustomFile extends CustomFile<JaluCustomFile> {

    private final @NotNull Class<? extends SettingsHolder>[] holders;

    private SettingsManager settingsManager;

    @SafeVarargs
    public JaluCustomFile(@NotNull final Path path, @NotNull Class<? extends SettingsHolder>... holders) {
        super(path);

        this.holders = holders;
    }

    @Override
    public final JaluCustomFile build() {
        final SettingsManagerBuilder builder = SettingsManagerBuilder.withYamlFile(getPath(), this.options != null ? options : YamlFileResourceOptions.builder().indentationSize(2).build());

        if (this.service != null) {
            builder.migrationService(this.service);
        } else {
            builder.useDefaultMigrationService();
        }

        if (this.holders != null) {
            builder.configurationData(this.holders);
        }

        this.settingsManager = builder.create();

        return this;
    }

    @Override
    public JaluCustomFile load() {
        if (this.settingsManager == null) {
            throw new FusionException("There was no settings manager available for " + getFileName());
        }

        this.settingsManager.reload();

        return this;
    }

    @Override
    public JaluCustomFile save() {
        if (this.settingsManager == null) {
            throw new FusionException("There was no settings manager available for " + getFileName());
        }

        this.settingsManager.save();

        return this;
    }

    @Override
    public final boolean isLoaded() {
        return this.settingsManager != null;
    }

    @Override
    public FileType getType() {
        return FileType.JALU;
    }

    private YamlFileResourceOptions options;

    public final JaluCustomFile setOptions(@NotNull final YamlFileResourceOptions options) {
        this.options = options;

        return this;
    }

    private MigrationService service;

    public final JaluCustomFile setService(@NotNull final MigrationService service) {
        this.service = service;

        return this;
    }

    public final SettingsManager getSettingsManager() {
        return this.settingsManager;
    }
}