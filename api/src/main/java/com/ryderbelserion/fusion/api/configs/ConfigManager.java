package com.ryderbelserion.fusion.api.configs;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.api.exceptions.FusionException;
import java.nio.file.Path;

public class ConfigManager {

    private final Path dataFolder;

    public ConfigManager(final Path dataFolder) {
        this.dataFolder = dataFolder;
    }

    private SettingsManager config;

    @SafeVarargs
    public final void init(final Class<? extends SettingsHolder>... classes) {
        final YamlFileResourceOptions builder = YamlFileResourceOptions.builder().indentationSize(2).build();

        this.config = SettingsManagerBuilder.withYamlFile(this.dataFolder.resolve("fusion.yml"), builder)
                .useDefaultMigrationService()
                .configurationData(classes)
                .create();
    }

    public void reload() {
        if (this.config == null) {
            throw new FusionException("fusion.yml is not available.");
        }

        this.config.reload();
    }

    public final SettingsManager getConfig() {
        return this.config;
    }
}