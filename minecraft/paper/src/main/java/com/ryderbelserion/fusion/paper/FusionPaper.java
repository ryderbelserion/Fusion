package com.ryderbelserion.fusion.paper;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.ryderbelserion.fusion.paper.builders.gui.GuiManager;
import com.ryderbelserion.fusion.paper.builders.gui.types.GuiListener;
import com.ryderbelserion.fusion.paper.files.PaperFileManager;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
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
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.util.Optional;
import java.util.UUID;

public class FusionPaper extends FusionKyori<Audience> {

    private final PaperFileManager fileManager;
    private final ComponentLogger logger;

    private PluginManager pluginManager;
    private GuiManager guiManager;
    private JavaPlugin plugin;
    private Server server;

    public FusionPaper(@NonNull final JavaPlugin plugin) {
        super(plugin.getDataPath());

        this.fileManager = new PaperFileManager(this.getDataPath());
        this.logger = plugin.getComponentLogger();
        this.server = plugin.getServer();
        this.plugin = plugin;

        this.pluginManager = this.server.getPluginManager();
    }

    public FusionPaper(@NonNull final BootstrapContext context) {
        super(context.getDataDirectory());

        this.fileManager = new PaperFileManager(this.getDataPath());
        this.logger = context.getLogger();
    }

    private HeadDatabaseAPI api;

    @Override
    public final FusionPaper init() {
        super.init();

        this.guiManager = new GuiManager();

        if (this.pluginManager.isPluginEnabled("HeadDatabaseAPI") && this.api == null) {
            this.api = new  HeadDatabaseAPI();
        }

        this.pluginManager.registerEvents(new GuiListener(), this.plugin);

        return this;
    }

    public FusionPaper setPlugin(@NonNull final JavaPlugin plugin) {
        this.server = plugin.getServer();
        this.pluginManager = this.server.getPluginManager();

        this.plugin = plugin;

        return this;
    }

    @Override
    public String papi(@Nullable final Audience sender, @NonNull final String message) {
        return isPluginEnabled("PlaceholderAPI") && sender instanceof Player player ? PlaceholderAPI.setPlaceholders(player, message) : message;
    }

    @Override
    public void registerPermission(@NonNull final PermissionContext context) {
        final Permission permission = new Permission(context.getPermission(), context.getDescription(), switch (context.getType()) {
            case TRUE -> PermissionDefault.TRUE;
            case FALSE -> PermissionDefault.FALSE;
            case OP -> PermissionDefault.OP;
            case NOT_OP -> PermissionDefault.NOT_OP;
        }, context.getChildren());

        this.pluginManager.addPermission(permission);
    }

    @Override
    public void log(@NonNull final Level level, @NonNull final String message, @NonNull final Exception exception, @NonNull final Object... args) {
        if (!this.isVerbose()) return;

        final Component component = asComponent(message.formatted(args));

        switch (level) {
            case WARNING -> this.logger.warn(component, exception);
            case ERROR -> this.logger.error(component, exception);
            case INFO -> this.logger.info(component, exception);
        }
    }

    @Override
    public void log(@NonNull final Level level, @NonNull final String message, @NonNull final Object... args) {
        if (!this.isVerbose()) return;

        final Component component = asComponent(message.formatted(args));

        switch (level) {
            case WARNING -> this.logger.warn(component);
            case ERROR -> this.logger.error(component);
            case INFO -> this.logger.info(component);
        }
    }

    @Override
    public final boolean isModReady(@NonNull final FusionKey key) {
        return this.pluginManager.isPluginEnabled(key.getValue());
    }

    @Override
    public @NonNull final String getNamespace() {
        return this.plugin.namespace();
    }

    @Override
    public @NonNull final PaperFileManager getFileManager() {
        return this.fileManager;
    }

    public @NonNull final Optional<HeadDatabaseAPI> getHeadApi() {
        return Optional.ofNullable(this.api);
    }

    public @NonNull final GuiManager getGuiManager() {
        return this.guiManager;
    }

    public @NonNull final Server getServer() {
        return this.server;
    }

    public @NonNull final PlayerProfile createProfile(@NonNull final UUID uuid, @Nullable final String name) {
        return this.server.createProfile(uuid, name);
    }

    public final boolean isPluginEnabled(@NonNull final String plugin) {
        return this.pluginManager.isPluginEnabled(plugin);
    }
}