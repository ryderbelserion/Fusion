package com.ryderbelserion;

import com.ryderbelserion.api.exception.FusionException;
import com.ryderbelserion.paper.Fusion;
import com.ryderbelserion.paper.builder.gui.listeners.GuiListener;
import com.ryderbelserion.paper.enums.Support;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FusionSettings {

    private static final FusionSettings instance = new FusionSettings();

    public static FusionSettings get() {
        return instance;
    }

    private HeadDatabaseAPI headDatabaseAPI = null;
    private boolean isRegistered;
    private Plugin plugin = null;
    private Fusion fusion = null;

    public void onEnable(@NotNull Plugin plugin) {
        if (this.isRegistered) return;

        this.isRegistered = true;
        this.plugin = plugin;

        this.fusion = new Fusion();
        this.fusion.onEnable();

        if (Support.head_database.isEnabled()) {
            this.headDatabaseAPI = new HeadDatabaseAPI();
        }

        final Server server = this.plugin.getServer();
        final PluginManager manager = server.getPluginManager();

        manager.registerEvents(new GuiListener(), this.plugin);
    }

    public void onReload() {
        this.fusion.onReload();
    }

    public void onDisable() {
        this.fusion.onDisable();
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

    public @Nullable HeadDatabaseAPI getDatabaseAPI() {
        return this.headDatabaseAPI;
    }
}