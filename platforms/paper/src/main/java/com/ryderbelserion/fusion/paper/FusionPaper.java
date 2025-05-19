package com.ryderbelserion.fusion.paper;

import com.ryderbelserion.fusion.adventure.FusionCore;
import com.ryderbelserion.fusion.paper.enums.Support;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

public class FusionPaper extends FusionCore {

    @Override
    public final String parsePlaceholders(final Audience audience, final String input) {
        return Support.placeholder_api.isEnabled() && audience instanceof Player player ? PlaceholderAPI.setPlaceholders(player, input) : input;
    }

    @Override
    public final String chomp(final String message) {
        return StringUtils.chomp(message);
    }
}