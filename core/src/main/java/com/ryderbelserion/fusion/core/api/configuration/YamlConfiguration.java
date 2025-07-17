package com.ryderbelserion.fusion.core.api.configuration;

import org.spongepowered.configurate.yaml.internal.snakeyaml.DumperOptions;
import org.spongepowered.configurate.yaml.internal.snakeyaml.LoaderOptions;
import org.spongepowered.configurate.yaml.internal.snakeyaml.Yaml;

public class YamlConfiguration {

    private DumperOptions dumperOptions;
    private LoaderOptions loaderOptions;

    private final Yaml yaml;

    public YamlConfiguration() {
        this.yaml = new Yaml();
    }
}