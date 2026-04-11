package com.ryderbelserion.fusion.paper;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.ryderbelserion.fusion.paper.builders.gui.GuiManager;
import com.ryderbelserion.fusion.paper.builders.gui.types.GuiListener;
import com.ryderbelserion.fusion.paper.files.PaperFileManager;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FusionPaper extends FusionKyori<Audience> {

    private final PaperFileManager fileManager;
    private final PluginManager pluginManager;
    private final ComponentLogger logger;
    private final GuiManager guiManager;
    private final Server server;

    private JavaPlugin plugin;

    public FusionPaper(@NotNull final JavaPlugin plugin) {
        super(plugin.getDataPath());

        this.fileManager = new PaperFileManager(this.getDataPath());
        this.logger = plugin.getComponentLogger();
        this.guiManager = new GuiManager();
        this.server = plugin.getServer();
        this.plugin = plugin;

        this.pluginManager = this.server.getPluginManager();
    }

    private HeadDatabaseAPI api;

    @Override
    public final FusionPaper init() {
        super.init();

        if (this.pluginManager.isPluginEnabled("HeadDatabaseAPI") && this.api == null) {
            this.api = new  HeadDatabaseAPI();
        }

        this.server.getPluginManager().registerEvents(new GuiListener(), this.plugin);

        return this;
    }

    public FusionPaper setPlugin(@NotNull final JavaPlugin plugin) {
        this.plugin = plugin;

        return this;
    }

    @Override
    public String papi(@Nullable final Audience sender, @NotNull final String message) {
        return isPluginEnabled("PlaceholderAPI") && sender instanceof Player player ? PlaceholderAPI.setPlaceholders(player, message) : message;
    }

    @Override
    public void registerPermission(@NotNull final PermissionContext context) {
        final Permission permission = new Permission(context.getPermission(), context.getDescription(), switch (context.getType()) {
            case TRUE -> PermissionDefault.TRUE;
            case FALSE -> PermissionDefault.FALSE;
            case OP -> PermissionDefault.OP;
            case NOT_OP -> PermissionDefault.NOT_OP;
        }, context.getChildren());

        this.pluginManager.addPermission(permission);
    }

    @Override
    public void log(@NotNull final Level level, @NotNull final String message, @NotNull final Exception exception, @NotNull final Object... args) {
        if (!this.isVerbose()) return;

        final Component component = asComponent(message.formatted(args));

        switch (level) {
            case WARNING -> this.logger.warn(component, exception);
            case ERROR -> this.logger.error(component, exception);
            case INFO -> this.logger.info(component, exception);
        }
    }

    @Override
    public void log(@NotNull final Level level, @NotNull final String message, @NotNull final Object... args) {
        if (!this.isVerbose()) return;

        final Component component = asComponent(message.formatted(args));

        switch (level) {
            case WARNING -> this.logger.warn(component);
            case ERROR -> this.logger.error(component);
            case INFO -> this.logger.info(component);
        }
    }

    @Override
    public final boolean isModReady(@NotNull final FusionKey key) {
        return this.pluginManager.isPluginEnabled(key.getValue());
    }

    @Override
    public @NotNull final PaperFileManager getFileManager() {
        return this.fileManager;
    }

    public @NotNull final Optional<HeadDatabaseAPI> getHeadApi() {
        return Optional.ofNullable(this.api);
    }

    public @NotNull final GuiManager getGuiManager() {
        return this.guiManager;
    }

    public @NotNull final Server getServer() {
        return this.server;
    }

    public @NotNull final PlayerProfile createProfile(@NotNull final UUID uuid, @Nullable final String name) {
        return this.server.createProfile(uuid, name);
    }

    public final boolean isPluginEnabled(@NotNull final String plugin) {
        return this.pluginManager.isPluginEnabled(plugin);
    }
}