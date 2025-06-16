package com.ryderbelserion.fusion.paper.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PapiUtils {

    public @NotNull final String parse(@NotNull final Player player, @NotNull final String message) {
        return PlaceholderAPI.setPlaceholders(player, message);
    }
}