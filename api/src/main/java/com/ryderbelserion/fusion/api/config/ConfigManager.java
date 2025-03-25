package com.ryderbelserion.fusion.api.config;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.api.exception.FusionException;
import java.io.File;

public class ConfigManager {

    private final File dataFolder;

    public ConfigManager(final File dataFolder) {
        this.dataFolder = dataFolder;
    }

    private SettingsManager config;

    @SafeVarargs
    public final void init(final Class<? extends SettingsHolder>... classes) {
        final YamlFileResourceOptions builder = YamlFileResourceOptions.builder().indentationSize(2).build();

        this.config = SettingsManagerBuilder.withYamlFile(new File(this.dataFolder, "fusion.yml"), builder)
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