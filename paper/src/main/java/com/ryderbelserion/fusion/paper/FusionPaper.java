package com.ryderbelserion.fusion.paper;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.ryderbelserion.fusion.core.api.FusionCore;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.enums.Support;
import com.ryderbelserion.fusion.core.api.utils.keys.ConfigKeys;
import com.ryderbelserion.fusion.paper.api.builders.gui.listeners.GuiListener;
import com.ryderbelserion.fusion.paper.api.commands.PaperCommandManager;
import com.ryderbelserion.fusion.paper.api.structure.StructureRegistry;
import com.ryderbelserion.fusion.paper.files.FileManager;
import com.ryderbelserion.fusion.paper.utils.keys.PluginKeys;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

public class FusionPaper extends FusionCore {

    private PaperCommandManager commandManager;
    private StructureRegistry registry;
    private FileManager fileManager;
    private HeadDatabaseAPI api;
    private JavaPlugin plugin;
    private Server server;

    private PluginManager pluginManager;

    public FusionPaper(@NotNull final ComponentLogger logger, @NotNull final Path path) {
        super(logger, path, consumer -> consumer.useDefaultMigrationService().configurationData(ConfigKeys.class, PluginKeys.class));
    }

    public FusionPaper(@NotNull final JavaPlugin plugin) {
        this(plugin.getComponentLogger(), plugin.getDataPath());
    }

    public void enable(@NotNull final JavaPlugin plugin) {
        super.enable();

        this.plugin = plugin;
        this.server = this.plugin.getServer();
        this.pluginManager = this.server.getPluginManager();

        this.registry = new StructureRegistry(this.plugin);

        if (this.fileManager == null) {
            this.fileManager = new FileManager();
        }

        this.commandManager = new PaperCommandManager(this.plugin);

        if (Support.head_database.isEnabled() && this.api == null) {
            this.api = new HeadDatabaseAPI();
        }

        this.pluginManager.registerEvents(new GuiListener(), this.plugin);
    }

    @Override
    public void disable() {
        super.disable();

        this.commandManager.disable();
    }

    @Override
    public @NotNull final String parsePlaceholders(@NotNull final Audience audience, @NotNull final String message) {
        return Support.placeholder_api.isEnabled() && audience instanceof Player player ? PlaceholderAPI.setPlaceholders(player, message) : message;
    }

    @Override
    public @NotNull final PaperCommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public @NotNull final String chomp(@NotNull final String message) {
        return StringUtils.chomp(message);
    }

    @Override
    public final boolean isPluginEnabled(@NotNull final String name) {
        return this.pluginManager.isPluginEnabled(name);
    }

    @Override
    public @NotNull final PlayerProfile createProfile(@NotNull final UUID uuid, @Nullable final String name) {
        return this.server.createProfile(uuid, name);
    }

    @Override
    public @NotNull final FileManager getFileManager() {
        if (this.fileManager == null) {
            throw new FusionException("An error occurred while trying to get the file manager instance.");
        }

        return this.fileManager;
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

    public @NotNull final Optional<HeadDatabaseAPI> getHeadDatabaseAPI() {
        return Optional.ofNullable(this.api);
    }
}