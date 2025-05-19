package com.ryderbelserion.fusion.paper;

import com.ryderbelserion.fusion.adventure.FusionAdventure;
import com.ryderbelserion.fusion.paper.enums.Support;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import java.util.logging.Logger;

public class FusionPaper extends FusionAdventure {

    private final Plugin plugin;
    private final Logger logger;

    public FusionPaper(final Plugin plugin) {
        FusionPlugin.setPlugin(this.plugin = plugin);

        this.logger = this.plugin.getLogger();
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
}