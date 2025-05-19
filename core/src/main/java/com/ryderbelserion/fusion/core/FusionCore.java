package com.ryderbelserion.fusion.core;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.core.api.ConfigKeys;
import com.ryderbelserion.fusion.core.managers.ModuleManager;
import com.ryderbelserion.fusion.core.managers.PluginExtension;
import com.ryderbelserion.fusion.core.managers.files.FileManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.logging.Logger;

public abstract class FusionCore {

    protected final PluginExtension pluginExtension;
    protected final ModuleManager moduleManager;
    protected final FileManager fileManager;
    protected final SettingsManager config;

    protected final Logger logger;
    protected final Path dataPath;

    public FusionCore(@NotNull final Consumer<SettingsManagerBuilder> consumer, @NotNull final YamlFileResourceOptions options, @NotNull final Logger logger, @NotNull final Path dataPath) {
        Provider.register(this);

        this.dataPath = dataPath;
        this.logger = logger;

        final SettingsManagerBuilder builder = SettingsManagerBuilder.withYamlFile(this.dataPath.resolve("fusion.yml"), options);

        builder.useDefaultMigrationService();

        consumer.accept(builder); // overrides the default migration service if set in the consumer.

        this.config = builder.create();

        this.fileManager = new FileManager();

        this.pluginExtension = new PluginExtension();
        this.moduleManager = new ModuleManager();
    }

    public FusionCore(@NotNull final Consumer<SettingsManagerBuilder> consumer, @NotNull final Logger logger, @NotNull final Path dataPath) {
        this(consumer, YamlFileResourceOptions.builder().indentationSize(2).build(), logger, dataPath);
    }

    public FusionCore(@NotNull Logger logger, @NotNull Path dataPath) {
        this(consumer -> consumer.configurationData(ConfigKeys.class), YamlFileResourceOptions.builder().indentationSize(2).build(), logger, dataPath);
    }

    protected FusionCore() {
        this.pluginExtension = new PluginExtension();
        this.moduleManager = new ModuleManager();
        this.fileManager = new FileManager();

        this.config = null;
        this.dataPath = null;
        this.logger = null;
    }

    public <E> void registerEvent(@NotNull final E event) {

    }

    public @NotNull PluginExtension getPluginExtension() {
        return this.pluginExtension;
    }

    public @NotNull ModuleManager getModuleManager() {
        return this.moduleManager;
    }

    public @NotNull FileManager getFileManager() {
        return this.fileManager;
    }

    public abstract Logger getLogger();

    public @NotNull Path getDataPath() {
        return this.dataPath;
    }

    public void reload() {
        this.config.reload();
    }

    public void disable() {
        Provider.unregister();
    }

    public @Nullable<T> T getHeadDatabaseAPI() {
        return null;
    }

    public @NotNull String getItemsPlugin() {
        return "none";
    }

    public abstract @NotNull String chomp(@NotNull final String message);

    public @NotNull String getRoundingFormat() {
        return this.config.getProperty(ConfigKeys.rounding_format);
    }

    public int getRecursionDepth() {
        return this.config.getProperty(ConfigKeys.recursion_depth);
    }

    public @NotNull String getNumberFormat() {
        return this.config.getProperty(ConfigKeys.number_format);
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