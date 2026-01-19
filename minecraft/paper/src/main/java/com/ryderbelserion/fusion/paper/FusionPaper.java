package com.ryderbelserion.fusion.paper;

import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.kyori.FusionKyori;
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

public class FusionPaper extends FusionKyori<Audience> {

    private final ComponentLogger logger;
    private final JavaPlugin plugin;

    public FusionPaper(@NotNull final JavaPlugin plugin, @NotNull final Path source) {
        super(source, plugin.getDataPath());

        this.plugin = plugin;
        this.logger = this.plugin.getComponentLogger();
    }

    @Override
    public String papi(@Nullable final Audience sender, @NotNull final String message) {
        final Server server = this.plugin.getServer();
        final PluginManager pluginManager = server.getPluginManager();

        final boolean isPapiAvailable = pluginManager.isPluginEnabled("PlaceholderAPI");

        return isPapiAvailable && sender instanceof Player player ? PlaceholderAPI.setPlaceholders(player, message) : message;
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
}