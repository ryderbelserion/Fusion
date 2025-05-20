package com.ryderbelserion.fusion.core;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.core.api.ConfigKeys;
import com.ryderbelserion.fusion.core.api.events.EventBuilder;
import com.ryderbelserion.fusion.core.api.plugins.PluginBuilder;
import org.jetbrains.annotations.NotNull;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.logging.Logger;

public abstract class FusionCore {

    protected final PluginBuilder pluginBuilder;
    protected final EventBuilder eventBuilder;
    protected final SettingsManager config;

    protected final Logger logger;
    protected final Path path;

    public FusionCore(@NotNull final Logger logger, @NotNull final Path path, @NotNull final Consumer<SettingsManagerBuilder> consumer) {
        Provider.register(this);

        this.logger = logger;
        this.path = path;

        final SettingsManagerBuilder builder = SettingsManagerBuilder.withYamlFile(this.path.resolve("fusion.yml"), YamlFileResourceOptions.builder().charset(StandardCharsets.UTF_8).indentationSize(2).build());

        builder.useDefaultMigrationService();

        consumer.accept(builder); // overrides the default migration service if set in the consumer.

        this.config = builder.create();

        this.pluginBuilder = new PluginBuilder();
        this.eventBuilder = new EventBuilder();
    }

    public <E> void registerEvent(@NotNull final E event) {

    }

    public void reload() {
        this.config.reload();
    }

    public void disable() {
        Provider.unregister();
    }

    public PluginBuilder getPluginBuilder() {
        return this.pluginBuilder;
    }

    public EventBuilder getEventBuilder() {
        return this.eventBuilder;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public Path getPath() {
        return this.path;
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

    public static class Provider {
        private static FusionCore core = null;

        public static void register(@NotNull final FusionCore core) {
            Provider.core = core;
        }

        public static void unregister() {
            Provider.core = null;
        }

        public static FusionCore get() {
            return core;
        }
    }
}