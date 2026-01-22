package com.ryderbelserion.fusion.paper;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FusionPaper extends FusionKyori<Audience> {

    private final PluginManager pluginManager;
    private final ComponentLogger logger;
    private final Server server;

    public FusionPaper(@NotNull final JavaPlugin plugin, @NotNull final Path source) {
        super(source, plugin.getDataPath());

        this.logger = plugin.getComponentLogger();
        this.server = plugin.getServer();

        this.pluginManager = this.server.getPluginManager();
    }

    private HeadDatabaseAPI api;

    @Override
    public final FusionCore init() {
        super.init();

        if (this.pluginManager.isPluginEnabled("HeadDatabaseAPI") && this.api == null) {
            this.api = new  HeadDatabaseAPI();
        }

        return this;
    }

    @Override
    public String papi(@Nullable final Audience sender, @NotNull final String message) {
        return isPluginEnabled("PlaceholderAPI") && sender instanceof Player player ? PlaceholderAPI.setPlaceholders(player, message) : message;
    }

    @Override
    public void log(@NotNull final Level level, @NotNull final String message, @NotNull final Map<String, String> placeholders) {
        if (!this.isVerbose()) return;

        final Component component = asComponent(message, placeholders);

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

    public @NotNull final Optional<HeadDatabaseAPI> getHeadApi() {
        return Optional.ofNullable(this.api);
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