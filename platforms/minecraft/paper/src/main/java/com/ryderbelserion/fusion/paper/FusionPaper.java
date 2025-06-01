package com.ryderbelserion.fusion.paper;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.ryderbelserion.fusion.core.api.addons.AddonManager;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.core.api.ConfigKeys;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.paper.api.PluginKeys;
import com.ryderbelserion.fusion.paper.api.builders.gui.listeners.GuiListener;
import com.ryderbelserion.fusion.kyori.enums.Support;
import com.ryderbelserion.fusion.paper.api.structure.StructureRegistry;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.nio.file.Path;
import java.util.UUID;

public class FusionPaper extends FusionKyori {

    private LegacyFileManager fileManager = null;
    private StructureRegistry registry = null;
    private HeadDatabaseAPI api = null;
    private Plugin plugin = null;
    private Server server = null;

    private PluginManager pluginManager = null;

    public FusionPaper(@NotNull final ComponentLogger logger, @NotNull final Path path) {
        super(logger, path, consumer -> consumer.useDefaultMigrationService().configurationData(ConfigKeys.class, PluginKeys.class));
    }

    public FusionPaper(@NotNull final Plugin plugin) {
        this(plugin.getComponentLogger(), plugin.getDataPath());

        this.plugin = plugin;
    }

    public void enable(@NotNull final Plugin plugin) {
        super.load();

        FusionPlugin.setPlugin(this.plugin = plugin);

        this.server = this.plugin.getServer();
        this.pluginManager = this.server.getPluginManager();

        this.registry = new StructureRegistry();

        if (Support.head_database.isEnabled() && this.api == null) {
            this.api = new HeadDatabaseAPI();
        }

        this.pluginManager.registerEvents(new GuiListener(), this.plugin);

        if (this.isAddonsEnabled()) {
            final AddonManager addonManager = getAddonManager();

            addonManager.load().enableAddons();

            this.logger.warn("Successfully enabled {} addons", addonManager.getAddons().size());
        }
    }

    @Override
    public @NotNull final String parsePlaceholders(@NotNull final Audience audience, @NotNull final String message) {
        return Support.placeholder_api.isEnabled() && audience instanceof Player player ? PlaceholderAPI.setPlaceholders(player, message) : message;
    }

    @Override
    public @NotNull final String chomp(@NotNull final String message) {
        return StringUtils.chomp(message);
    }

    @Override
    public final boolean isPluginEnabled(@NotNull String name) {
        return this.pluginManager.isPluginEnabled(name);
    }

    @Override
    public final PlayerProfile createProfile(@NotNull UUID uuid, @Nullable String name) {
        return this.server.createProfile(uuid, name);
    }

    public @NotNull final String getItemsPlugin() {
        return this.config.getProperty(PluginKeys.items_plugin);
    }

    public @NotNull final StructureRegistry getRegistry() {
        if (this.registry == null) {
            throw new FusionException("An error occurred while trying to get the structure registry instance.");
        }

        return this.registry;
    }

    public @NotNull final HeadDatabaseAPI getApi() {
        if (this.api == null) {
            throw new FusionException("HeadDatabaseAPI is not initialized.");
        }

        return this.api;
    }
}