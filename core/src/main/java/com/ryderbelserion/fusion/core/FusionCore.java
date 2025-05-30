package com.ryderbelserion.fusion.core;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.core.api.ConfigKeys;
import com.ryderbelserion.fusion.core.api.addons.AddonManager;
import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
import com.ryderbelserion.fusion.core.api.plugins.PluginBuilder;
import com.ryderbelserion.fusion.core.files.FileManager;
import org.jetbrains.annotations.NotNull;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.function.Consumer;

public abstract class FusionCore {

    protected final PluginBuilder pluginBuilder;
    protected final FileManager fileManager;
    protected final SettingsManager config;

    protected final AddonManager addonManager;

    protected final Path path;

    public FusionCore(@NotNull final Path path, @NotNull final Consumer<SettingsManagerBuilder> consumer) {
        Provider.register(this);

        this.path = path;

        final SettingsManagerBuilder builder = SettingsManagerBuilder.withYamlFile(this.path.resolve("fusion.yml"), YamlFileResourceOptions.builder().charset(StandardCharsets.UTF_8).indentationSize(2).build());

        builder.useDefaultMigrationService();

        consumer.accept(builder); // overrides the default migration service if set in the consumer.

        this.config = builder.create();

        this.pluginBuilder = new PluginBuilder();
        this.addonManager = new AddonManager(this.path);
        this.fileManager = new FileManager();
    }

    public ILogger getLogger() {
        return null;
    }

    public void reload() {
        this.config.reload();
    }

    public void disable() {
        Provider.unregister();

        this.addonManager.purge();
    }

    public PluginBuilder getPluginBuilder() {
        return this.pluginBuilder;
    }

    public AddonManager getAddonManager() {
        return this.addonManager;
    }

    public FileManager getFileManager() {
        return this.fileManager;
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

    public boolean isAddonsEnabled() {
        return this.config.getProperty(ConfigKeys.addon_system);
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