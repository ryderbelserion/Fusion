package com.ryderbelserion.paper.fusion;

import com.ryderbelserion.core.FusionLayout;
import com.ryderbelserion.paper.fusion.enums.Support;
import com.ryderbelserion.core.util.StringUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Fusion extends FusionLayout {

    private final FusionApi settings = FusionApi.get();

    private final Plugin plugin = this.settings.getPlugin();

    @Override
    public File getDataFolder() {
        return this.plugin.getDataFolder();
    }

    @Override
    public ComponentLogger getLogger() {
        return this.plugin.getComponentLogger();
    }

    @Override
    public @NotNull Component placeholders(@NotNull String line) {
        return placeholders(null, line, new HashMap<>());
    }

    @Override
    public @NotNull Component placeholders(@NotNull String line, @NotNull Map<String, String> placeholders) {
        return placeholders(null, line, placeholders);
    }

    @Override
    public @NotNull Component placeholders(@Nullable final Audience audience, @NotNull String line, @NotNull final Map<String, String> placeholders) {
        return placeholders(null, line, placeholders, null);
    }

    @Override
    public @NotNull Component placeholders(@Nullable final Audience audience, @NotNull String line, @NotNull final Map<String, String> placeholders, @Nullable final List<TagResolver> tags) {
        final List<TagResolver> resolvers = new ArrayList<>();

        if (tags != null) {
            resolvers.addAll(tags);
        }

        String clonedLine = line.replaceAll("\\{", "<").replaceAll("}", ">");

        /*for (final Map.Entry<String, String> placeholder : placeholders.entrySet()) {
            if (placeholder != null) {
                final String key = placeholder.getKey();
                final String value = placeholder.getValue();

                if (key != null && value != null) {
                    clonedLine = clonedLine.replace(key, value).replace(key.toLowerCase(), value);
                }
            }
        }*/

        placeholders.forEach((placeholder, value) -> {
            final TagResolver tag = Placeholder.parsed(placeholder.replaceAll("\\{", "").replaceAll("}", "").replaceAll("<", "").replaceAll(">", "").toLowerCase(), value);

            resolvers.add(tag);
        });

        if (audience instanceof Player player && Support.placeholder_api.isEnabled()) {
            clonedLine = PlaceholderAPI.setPlaceholders(player, clonedLine);
        }

        return StringUtils.parse(clonedLine, TagResolver.resolver(resolvers));
    }

    @Override
    public @NotNull Component color(@NotNull String line) {
        return color(null, line, new HashMap<>());
    }

    @Override
    public @NotNull Component color(@NotNull String line, @NotNull Map<String, String> placeholders) {
        return color(null, line, placeholders);
    }

    @Override
    public @NotNull Component color(@Nullable final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders) {
        return placeholders(audience, line, placeholders);
    }

    @Override
    public void sendMessage(@NotNull final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders) {
        audience.sendMessage(color(audience, line, placeholders));
    }

    @Override
    public void sendMessage(@NotNull final Audience audience, @NotNull final List<String> lines, @NotNull final Map<String, String> placeholders) {
        sendMessage(audience, StringUtils.toString(lines), placeholders);
    }

    @Override
    public String chomp(@NotNull final String message) {
        return org.apache.commons.lang3.StringUtils.chomp(message);
    }
}