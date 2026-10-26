package com.ryderbelserion.fusion.config;

import com.ryderbelserion.fusion.api.enums.Level;
import com.ryderbelserion.fusion.config.keys.ConfigKeys;
import com.ryderbelserion.fusion.config.migration.BeidouMigration;
import com.ryderbelserion.fusion.core.api.config.ConfigBuilder;
import com.ryderbelserion.fusion.core.api.config.properties.PropertyManager;
import com.ryderbelserion.fusion.core.api.config.properties.objects.interfaces.Property;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.yaml.enums.NodeStyle;
import java.nio.file.Path;
import java.util.Arrays;

public class ConfigManager {

    private final FusionPaper fusion;
    private final Path path;

    public ConfigManager(final FusionPaper fusion, final Path path) {
        this.fusion = fusion;
        this.path = path;
    }

    public void init() {
        final PropertyManager builder = ConfigBuilder.withYamlPath(this.path.resolve("beidou.yml"))
                .configuration(ConfigKeys.class)
                .withNodeStyle(NodeStyle.BLOCK)
                .withHeaderMode(HeaderMode.PRESERVE)
                .withIndent(1)
                .withMigrationService(new BeidouMigration())
                .create();

        final Property<Boolean> property = ConfigKeys.test_boolean;

        this.fusion.log(Level.info, "<gold>Value: %s, <green>Alias Path: %s", builder.getProperty(property), Arrays.toString(builder.getAlias("test", "toggle")));
    }
}