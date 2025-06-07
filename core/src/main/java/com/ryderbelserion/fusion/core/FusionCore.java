package com.ryderbelserion.fusion.core;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.core.api.ConfigKeys;
import com.ryderbelserion.fusion.core.api.addons.AddonManager;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
import com.ryderbelserion.fusion.core.api.plugins.PluginBuilder;
import com.ryderbelserion.fusion.core.files.FileManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * Handles the core of the library!
 */
public abstract class FusionCore {

    protected final SettingsManager config;

    private final Path path;

    protected FusionCore(@NotNull final Path path, @NotNull final Consumer<SettingsManagerBuilder> consumer) {
        Provider.register(this);

        this.path = path;

        final SettingsManagerBuilder builder = SettingsManagerBuilder.withYamlFile(this.path.resolve("fusion.yml"), YamlFileResourceOptions.builder().charset(StandardCharsets.UTF_8).indentationSize(2).build());

        builder.useDefaultMigrationService();

        consumer.accept(builder); // overrides the default migration service if set in the consumer.

        this.config = builder.create();
    }

    private AddonManager addonManager;
    private PluginBuilder pluginBuilder;
    private FileManager fileManager;

    public void load() {
        if (this.pluginBuilder == null) {
            this.pluginBuilder = new PluginBuilder();
        }

        if (this.fileManager == null) {
            this.fileManager = new FileManager();
        }

        if (this.addonManager == null) {
            this.addonManager = new AddonManager();
        }
    }

    public @NotNull abstract ILogger getLogger();

    public void reload(final boolean isHotReload) {
        this.config.reload();

        if (this.isAddonsEnabled()) {
            this.addonManager.getAddons().forEach(addon -> {
                if (isHotReload) {
                    this.addonManager.reloadAddon(addon);
                } else {
                    this.addonManager.reloadAddonConfig(addon);
                }
            });
        }
    }

    public void disable() {
        Provider.unregister();

        this.addonManager.purge();
    }

    public <E> void registerEvent(@NotNull final E event) {

    }

    public @NotNull final PluginBuilder getPluginBuilder() {
        return this.pluginBuilder;
    }

    public @NotNull final AddonManager getAddonManager() {
        return this.addonManager;
    }

    public @NotNull final FileManager getFileManager() {
        return this.fileManager;
    }

    public @NotNull String getRoundingFormat() {
        return this.config.getProperty(ConfigKeys.rounding_format);
    }

    public @NotNull String getNumberFormat() {
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

    public @NotNull final Path getPath() {
        return this.path;
    }

    public static class Provider {
        private static @Nullable FusionCore core;

        public static void register(@NotNull final FusionCore core) {
            Provider.core = core;
        }

        public static void unregister() {
            Provider.core = null;
        }

        public static @NotNull FusionCore get() {
            if (core == null) {
                throw new FusionException("FusionCore is not initialized.");
            }

            return core;
        }
    }
}