package com.ryderbelserion.fusion.paper;

import com.ryderbelserion.fusion.adventure.FusionCore;
import com.ryderbelserion.fusion.paper.enums.Support;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FusionPaper extends FusionCore {

    @Override
    public final String parsePlaceholders(@NotNull final Audience audience, @NotNull final String input) {
        return Support.placeholder_api.isEnabled() && audience instanceof Player player ? PlaceholderAPI.setPlaceholders(player, input) : input;
    }

    @Override
    public final String chomp(@NotNull final String message) {
        return StringUtils.chomp(message);
    }
}