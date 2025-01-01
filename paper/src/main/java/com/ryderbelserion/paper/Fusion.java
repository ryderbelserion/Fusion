package com.ryderbelserion.paper;

import com.ryderbelserion.core.FusionLayout;
import com.ryderbelserion.core.files.FileManager;
import com.ryderbelserion.paper.enums.Support;
import com.ryderbelserion.core.util.Methods;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Fusion extends FusionLayout {

    private final FusionApi settings = FusionApi.get();

    private final Plugin plugin = this.settings.getPlugin();

    private final FileManager fileManager;

    public Fusion() {
        this.fileManager = new FileManager();
    }

    @Override
    public File getDataFolder() {
        return this.plugin.getDataFolder();
    }

    @Override
    public ComponentLogger getLogger() {
        return this.plugin.getComponentLogger();
    }

    @Override
    public FileManager getFileManager() {
        return this.fileManager;
    }

    @Override
    public @NotNull String placeholders(@NotNull String line) {
        return placeholders(null, line, new HashMap<>());
    }

    @Override
    public @NotNull String placeholders(@NotNull String line, @NotNull Map<String, String> placeholders) {
        return placeholders(null, line, placeholders);
    }

    @Override
    public @NotNull String placeholders(@Nullable final Audience audience, @NotNull String line, @NotNull final Map<String, String> placeholders) {
        if (audience != null && Support.placeholder_api.isEnabled()) {
            if (audience instanceof Player player) {
                line = PlaceholderAPI.setPlaceholders(player, line);
            }
        }

        if (!placeholders.isEmpty()) {
            for (final Map.Entry<String, String> placeholder : placeholders.entrySet()) {

                if (placeholder != null) {
                    final String key = placeholder.getKey();
                    final String value = placeholder.getValue();

                    if (key != null && value != null) {
                        line = line.replace(key, value).replace(key.toLowerCase(), value);
                    }
                }
            }
        }

        return line;
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
        return Methods.parse(placeholders(audience, line, placeholders));
    }

    @Override
    public void sendMessage(@NotNull final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders) {
        audience.sendMessage(color(audience, line, placeholders));
    }

    @Override
    public void sendMessage(@NotNull final Audience audience, @NotNull final List<String> lines, @NotNull final Map<String, String> placeholders) {
        sendMessage(audience, Methods.toString(lines), placeholders);
    }

    @Override
    public String chomp(@NotNull final String message) {
        return StringUtils.chomp(message);
    }
}