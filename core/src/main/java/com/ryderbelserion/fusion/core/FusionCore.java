package com.ryderbelserion.fusion.core;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.core.api.ConfigKeys;
import org.jetbrains.annotations.NotNull;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.logging.Logger;

public abstract class FusionCore {

    protected final SettingsManager config;

    protected final Logger logger;
    protected final Path path;

    public FusionCore(@NotNull final Logger logger, @NotNull final Path path, @NotNull final Consumer<SettingsManagerBuilder> consumer) {
        this.logger = logger;
        this.path = path;

        final SettingsManagerBuilder builder = SettingsManagerBuilder.withYamlFile(this.path.resolve("fusion.yml"), YamlFileResourceOptions.builder().charset(StandardCharsets.UTF_8).indentationSize(2).build());

        builder.useDefaultMigrationService();

        consumer.accept(builder); // overrides the default migration service if set in the consumer.

        this.config = builder.create();
    }

    public void reload() {
        this.config.reload();
    }

    public String getRoundingFormat() {
        return this.config.getProperty(ConfigKeys.rounding_format);
    }

    public String getNumberFormat() {
        return this.config.getProperty(ConfigKeys.number_format);
    }

    public int getRecursionDepth() {
        return this.config.getProperty(ConfigKeys.recursion_depth);
    }

    public boolean isVerbose() {
        return this.config.getProperty(ConfigKeys.is_verbose);
    }
}