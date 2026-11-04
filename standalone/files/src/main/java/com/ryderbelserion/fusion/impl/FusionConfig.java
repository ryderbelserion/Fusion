package com.ryderbelserion.fusion.impl;

import com.ryderbelserion.fusion.config.ConfigBuilder;
import com.ryderbelserion.fusion.config.properties.PropertyManager;
import com.ryderbelserion.fusion.impl.keys.RootKeys;
import com.ryderbelserion.fusion.impl.objects.ConfigProvider;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.yaml.enums.NodeStyle;
import java.nio.file.Path;

public class FusionConfig {

    private final Path path;

    public FusionConfig(final Path path) {
        this.path = path;
    }

    private PropertyManager config;

    public void build() {
        this.config = ConfigBuilder.withYamlPath(this.path).configuration(RootKeys.class).withHeaderMode(HeaderMode.PRESERVE).withIndent(1).withNodeStyle(NodeStyle.BLOCK).create();

        ConfigProvider.register(this);
    }

    public void reload() {
        this.config.reload();
    }

    public final PropertyManager getConfig() {
        return this.config;
    }
}