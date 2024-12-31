package com.ryderbelserion;

import com.ryderbelserion.core.api.exception.FusionException;
import com.ryderbelserion.paper.Fusion;
import com.ryderbelserion.paper.builder.gui.listeners.GuiListener;
import com.ryderbelserion.paper.enums.Support;
import com.ryderbelserion.paper.files.FileManager;
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

    public void enable(@NotNull Plugin plugin) {
        if (this.isRegistered) return;

        this.isRegistered = true;
        this.plugin = plugin;

        this.fusion = new Fusion();
        this.fusion.enable();

        this.fileManager = new FileManager();

        if (Support.head_database.isEnabled()) {
            this.headDatabaseAPI = new HeadDatabaseAPI();
        }

        final Server server = this.plugin.getServer();
        final PluginManager manager = server.getPluginManager();

        manager.registerEvents(new GuiListener(), this.plugin);
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
}