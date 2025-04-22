package com.ryderbelserion.fusion.paper;

import com.ryderbelserion.fusion.core.api.ConfigKeys;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.utils.AdvUtils;
import com.ryderbelserion.fusion.paper.api.builder.gui.listeners.GuiListener;
import com.ryderbelserion.fusion.paper.api.structure.StructureRegistry;
import com.ryderbelserion.fusion.paper.api.enums.Support;
import com.ryderbelserion.fusion.paper.files.LegacyFileManager;
import com.ryderbelserion.fusion.paper.api.PluginKeys;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FusionPaper extends FusionCore {

    public FusionPaper(@NotNull final ComponentLogger logger, @NotNull final Path dataFolder) {
        super(consumer -> consumer.useDefaultMigrationService().configurationData(ConfigKeys.class, PluginKeys.class), logger, dataFolder);
    }

    private boolean isRegistered = false;

    private LegacyFileManager fileManager = null;
    private StructureRegistry registry;
    private HeadDatabaseAPI heads;

    public void enable(@NotNull final Plugin plugin) {
        if (this.isRegistered) return;

        FusionPlugin.setPlugin(plugin);

        this.registry = new StructureRegistry();

        if (this.fileManager == null) {
            this.fileManager = new LegacyFileManager();
        }

        if (Support.head_database.isEnabled()) {
            this.heads = new HeadDatabaseAPI();
        }

        final Server server = plugin.getServer();
        final PluginManager manager = server.getPluginManager();

        manager.registerEvents(new GuiListener(), plugin);

        this.isRegistered = true;
    }

    @Override
    public void disable() {
        if (!this.isRegistered) return;

        super.disable();
    }

    public @NotNull final LegacyFileManager getLegacyFileManager() {
        if (this.fileManager == null) {
            throw new FusionException("An error occurred while trying to get the legacy file manager instance.");
        }

        return this.fileManager;
    }

    public @NotNull final StructureRegistry getRegistry() {
        return this.registry;
    }

    @Override
    public @NotNull final HeadDatabaseAPI getHeadDatabaseAPI() {
        if (this.heads == null) {
            throw new FusionException("HeadDatabaseAPI is not initialized.");
        }

        return this.heads;
    }

    @Override
    public @NotNull final Component placeholders(@Nullable final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders, @Nullable final List<TagResolver> tags) {
        final List<TagResolver> resolvers = new ArrayList<>();

        if (tags != null) {
            resolvers.addAll(tags);
        }

        String clonedLine = line.replaceAll("\\{", "<").replaceAll("}", ">");

        placeholders.forEach((placeholder, value) -> {
            final TagResolver tag = Placeholder.parsed(placeholder.replaceAll("\\{", "").replaceAll("}", "").replaceAll("<", "").replaceAll(">", "").toLowerCase(), value);

            resolvers.add(tag);
        });

        if (audience instanceof Player player && Support.placeholder_api.isEnabled()) {
            clonedLine = PlaceholderAPI.setPlaceholders(player, clonedLine);
        }

        return AdvUtils.parse(clonedLine, TagResolver.resolver(resolvers));
    }

    @Override
    public @NotNull final Component placeholders(@Nullable final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders) {
        return placeholders(null, line, placeholders, null);
    }

    @Override
    public @NotNull final Component placeholders(@NotNull final String line, @NotNull final Map<String, String> placeholders) {
        return placeholders(null, line, placeholders);
    }

    @Override
    public @NotNull final Component placeholders(@NotNull final String line) {
        return placeholders(null, line, new HashMap<>());
    }
    @Override
    public @NotNull final Component color(@Nullable final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders) {
        return placeholders(audience, line, placeholders);
    }

    @Override
    public @NotNull final Component color(@NotNull final String line, @NotNull final Map<String, String> placeholders) {
        return color(null, line, placeholders);
    }

    @Override
    public @NotNull final Component color(@NotNull final String line) {
        return color(null, line, new HashMap<>());
    }

    @Override
    public @NotNull final String chomp(@NotNull final String message) {
        return StringUtils.chomp(message);
    }

    @Override
    public @NotNull final String getItemsPlugin() {
        return this.config.getProperty(PluginKeys.items_plugin);
    }

    @Override
    public void sendMessage(@NotNull final Audience audience, @NotNull final List<String> lines, @NotNull final Map<String, String> placeholders) {
        for (final String line : lines) {
            sendMessage(audience, line, placeholders);
        }
    }

    @Override
    public void sendMessage(@NotNull final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders) {
        audience.sendMessage(color(audience, line, placeholders));
    }
}