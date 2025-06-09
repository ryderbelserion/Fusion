package com.ryderbelserion.fusion.core;

import com.ryderbelserion.fusion.core.api.addons.AddonManager;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
import com.ryderbelserion.fusion.core.api.plugins.PluginBuilder;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Handles the core of the library!
 */
public abstract class FusionCore {

    protected final YamlConfigurationLoader loader;
    protected CommentedConfigurationNode config;

    private final Path path;

    protected FusionCore(@NotNull final Path path) {
        Provider.register(this);

        this.path = path;

        FileUtils.extract("fusion.yml", this.path, new ArrayList<>());
        
        this.loader = YamlConfigurationLoader.builder().path(this.path.resolve("fusion.yml")).build();
        
        try {
            this.config = this.loader.load();
        } catch (final ConfigurateException exception) {
            throw new FusionException("Failed to load fusion.yml", exception);
        }
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
        FileUtils.extract("fusion.yml", this.path, new ArrayList<>()); // extract if they delete it.

        try {
            this.config = this.loader.load();
        } catch (final ConfigurateException exception) {
            throw new FusionException("Failed to load fusion.yml", exception);
        }

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
        return this.config.node("settings", "rounding").getString("");
    }

    public @NotNull String getNumberFormat() {
        return this.config.node("settings", "number_format").getString("");
    }

    public int getRecursionDepth() {
        return this.config.node("settings", "recursion_depth").getInt(1);
    }

    public boolean isVerbose() {
        return this.config.node("settings", "is_verbose").getBoolean(false);
    }

    public boolean isAddonsEnabled() {
        return this.config.node("settings", "addon_system").getBoolean(false);
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