package com.ryderbelserion.fusion.paper;

import com.ryderbelserion.fusion.core.api.exception.FusionException;
import com.ryderbelserion.fusion.paper.builder.gui.listeners.GuiListener;
import com.ryderbelserion.fusion.paper.enums.Support;
import com.ryderbelserion.fusion.paper.files.FileManager;
import com.ryderbelserion.fusion.paper.modules.EventRegistry;
import com.ryderbelserion.fusion.paper.modules.ModuleLoader;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.configuration.PluginMeta;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FusionApi {

    private static final FusionApi instance = new FusionApi();

    public static FusionApi get() {
        return instance;
    }

    private HeadDatabaseAPI headDatabaseAPI = null;
    private FileManager fileManager = null;
    private boolean isRegistered = false;
    private Plugin plugin = null;
    private Fusion fusion = null;

    private ModuleLoader loader;

    public void enable(@NotNull final Plugin plugin) {
        if (this.isRegistered) return;

        this.plugin = plugin;

        if (this.fusion == null) {
            this.fusion = new Fusion();
            this.fusion.enable(plugin.getName());
        }

        if (this.fileManager == null) {
            this.fileManager = new FileManager();
        }

        if (Support.head_database.isEnabled()) {
            this.headDatabaseAPI = new HeadDatabaseAPI();
        }

        final Server server = this.plugin.getServer();
        final PluginManager manager = server.getPluginManager();

        this.loader = new ModuleLoader(new EventRegistry(this.plugin, server));

        manager.registerEvents(new GuiListener(), this.plugin);

        this.isRegistered = true;
    }

    public void bootstrap(@NotNull final BootstrapContext context) {
        if (this.isRegistered) return;

        final PluginMeta pluginMeta = context.getPluginMeta();

        this.fusion = new Fusion();
        this.fusion.enable(pluginMeta.getName());

        this.fileManager = new FileManager();
    }

    public void reload() {
        this.fusion.reload();
    }

    public void disable() {
        this.fusion.disable();
    }

    public @NotNull Plugin getPlugin() {
        if (this.plugin == null) {
            throw new FusionException("An error occurred while trying to get the plugin instance.");
        }

        return this.plugin;
    }

    public @NotNull Fusion getFusion() {
        if (this.fusion == null) {
            throw new FusionException("An error occurred while trying to get the fusion instance.");
        }

        return this.fusion;
    }

    public @NotNull FileManager getFileManager() {
        if (this.fileManager == null) {
            throw new FusionException("An error occurred while trying to get the file manager instance.");
        }

        return this.fileManager;
    }

    public @Nullable HeadDatabaseAPI getDatabaseAPI() {
        return this.headDatabaseAPI;
    }

    public @NotNull ModuleLoader getLoader() {
        return this.loader;
    }
}