package com.ryderbelserion.fusion.paper;

import com.ryderbelserion.fusion.adventure.FusionAdventure;
import com.ryderbelserion.fusion.core.api.ConfigKeys;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.paper.api.PluginKeys;
import com.ryderbelserion.fusion.paper.api.builders.gui.listeners.GuiListener;
import com.ryderbelserion.fusion.paper.api.enums.Support;
import com.ryderbelserion.fusion.paper.api.structure.StructureRegistry;
import com.ryderbelserion.fusion.paper.files.LegacyFileManager;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.logging.Logger;

public class FusionPaper extends FusionAdventure {

    private LegacyFileManager fileManager = null;
    private StructureRegistry registry = null;
    private HeadDatabaseAPI api = null;
    private Plugin plugin = null;

    public FusionPaper(@NotNull final Logger logger, @NotNull final Path path) {
        super(logger, path, consumer -> consumer.useDefaultMigrationService().configurationData(ConfigKeys.class, PluginKeys.class));
    }

    public FusionPaper(@NotNull final Plugin plugin) {
        this(plugin.getLogger(), plugin.getDataPath());

        enable(plugin);
    }

    public void enable(@NotNull final Plugin plugin) {
        FusionPlugin.setPlugin(this.plugin = plugin);

        this.registry = new StructureRegistry();

        if (this.fileManager == null) {
            this.fileManager = new LegacyFileManager();
        }

        if (Support.head_database.isEnabled() && this.api == null) {
            this.api = new HeadDatabaseAPI();
        }

        this.plugin.getServer().getPluginManager().registerEvents(new GuiListener(), this.plugin);
    }

    @Override
    public @NotNull final String parsePlaceholders(@NotNull final Audience audience, @NotNull final String input) {
        return Support.placeholder_api.isEnabled() && audience instanceof Player player ? PlaceholderAPI.setPlaceholders(player, input) : input;
    }

    @Override
    public @NotNull final Logger getLogger() {
        return this.logger;
    }

    @Override
    public @NotNull final String chomp(@NotNull final String message) {
        return StringUtils.chomp(message);
    }

    public @NotNull final String getItemsPlugin() {
        return this.config.getProperty(PluginKeys.items_plugin);
    }

    public @NotNull final LegacyFileManager getLegacyFileManager() {
        if (this.fileManager == null) {
            throw new FusionException("An error occurred while trying to get the legacy file manager instance.");
        }

        return this.fileManager;
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