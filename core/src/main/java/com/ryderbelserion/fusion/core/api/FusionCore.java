package com.ryderbelserion.fusion.core.api;

import com.ryderbelserion.fusion.core.FusionLogger;
import com.ryderbelserion.fusion.core.plugins.FusionPluginManager;
import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.api.commands.ICommandManager;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.interfaces.IFileManager;
import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
import com.ryderbelserion.fusion.core.api.interfaces.plugins.IPluginManager;
import com.ryderbelserion.fusion.core.api.utils.AdvUtils;
import com.ryderbelserion.fusion.core.api.utils.FileUtils;
import com.ryderbelserion.fusion.core.api.utils.StringUtils;
import com.ryderbelserion.fusion.core.files.FileManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class FusionCore {

    protected YamlConfigurationLoader loader;
    protected CommentedConfigurationNode config;

    private final IPluginManager pluginManager;
    private final IFileManager fileManager;
    private final ILogger logger;
    private final Path path;

    public FusionCore(@NotNull final ComponentLogger logger, @NotNull final Path path) {
        FusionProvider.register(this);

        if (!Files.exists(path)) {
            path.toFile().mkdirs();
        }

        FileUtils.extract("fusion.yml", path, new ArrayList<>());

        this.loader = YamlConfigurationLoader.builder().path(path.resolve("fusion.yml")).build();

        try {
            this.config = this.loader.load();
        } catch (final ConfigurateException exception) {
            throw new FusionException("Failed to load fusion.yml", exception);
        }

        this.logger = new FusionLogger(logger);
        this.path = path;

        this.pluginManager = new FusionPluginManager();
        this.fileManager = new FileManager();
    }

    public abstract String parsePlaceholders(@NotNull final Audience audience, @NotNull final String message);

    public abstract ICommandManager getCommandManager();

    public boolean isPluginEnabled(final String name) {
        return false;
    }

    public <T> @Nullable T createProfile(@NotNull final UUID uuid, @Nullable final String name) {
        return null;
    }

    public @NotNull Component color(@NotNull final Audience audience, @NotNull final String message, @NotNull final Map<String, String> placeholders, @NotNull final List<TagResolver> tags) {
        final List<TagResolver> resolvers = new ArrayList<>(tags);

        placeholders.forEach((placeholder, value) -> resolvers.add(Placeholder.parsed(StringUtils.replaceAllBrackets(placeholder).toLowerCase(), value)));

        return AdvUtils.parse(parsePlaceholders(audience, StringUtils.replaceBrackets(message)), TagResolver.resolver(resolvers));
    }

    public @NotNull Component color(@NotNull final Audience audience, @NotNull final String message, @NotNull final Map<String, String> placeholders) {
        return color(audience, message, placeholders, List.of());
    }

    public @NotNull Component color(@NotNull final String message, @NotNull final Map<String, String> placeholders) {
        return color(Audience.empty(), message, placeholders);
    }

    public @NotNull Component color(@NotNull final String message) {
        return color(Audience.empty(), message, new HashMap<>());
    }

    public void sendMessage(@NotNull final Audience audience, @NotNull final String message, @NotNull final Map<String, String> placeholders) {
        audience.sendMessage(color(audience, message, placeholders));
    }

    public void sendMessage(@NotNull final Audience audience, @NotNull final List<String> messages, @NotNull final Map<String, String> placeholders) {
        messages.forEach(message -> sendMessage(audience, message, placeholders));
    }

    public void enable() {

    }

    public void reload() {
        FileUtils.extract("fusion.yml", getPath(), new ArrayList<>()); // extract if they delete it.

        try {
            this.config = this.loader.load();
        } catch (final ConfigurateException exception) {
            throw new FusionException("Failed to load fusion.yml", exception);
        }
    }

    public void disable() {
        FusionProvider.unregister();
    }

    public @NotNull final FusionPluginManager getPluginManager() {
        return (FusionPluginManager) this.pluginManager;
    }

    public @NotNull final CommentedConfigurationNode getConfig() {
        return this.config;
    }

    public @NotNull final IFileManager getFileManager() {
        return this.fileManager;
    }

    public @NotNull final String getRoundedFormat() {
        return this.config.node("settings", "rounding").getString("");
    }

    public @NotNull final String getNumberFormat() {
        return this.config.node("settings", "number_format").getString("");
    }

    public @NotNull final ILogger getLogger() {
        return this.logger;
    }

    public @NotNull final Path getPath() {
        return this.path;
    }

    public final boolean isVerbose() {
        return this.config.node("settings", "is_verbose").getBoolean(false);
    }

    public final int getDepth() {
        return this.config.node("settings", "recursion_depth").getInt(1);
    }
}