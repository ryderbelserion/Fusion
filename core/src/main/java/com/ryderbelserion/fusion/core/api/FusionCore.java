package com.ryderbelserion.fusion.core.api;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.core.api.utils.keys.ConfigKeys;
import com.ryderbelserion.fusion.core.plugins.FusionPluginManager;
import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.api.commands.ICommandManager;
import com.ryderbelserion.fusion.core.api.utils.AdvUtils;
import com.ryderbelserion.fusion.core.api.utils.StringUtils;
import com.ryderbelserion.fusion.core.files.FileManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class FusionCore {

    protected final SettingsManager config;

    private final FusionPluginManager pluginManager;
    private final ComponentLogger logger;

    private final FileManager fileManager;
    private final Path path;

    public FusionCore(@NotNull final ComponentLogger logger, @NotNull final Path path, @NotNull final Consumer<SettingsManagerBuilder> consumer) {
        this.logger = logger;
        this.path = path;

        FusionProvider.register(this);

        if (!Files.exists(path)) {
            path.toFile().mkdirs();
        }

        // create fusion.yml
        final SettingsManagerBuilder builder = SettingsManagerBuilder.withYamlFile(this.path.resolve("fusion.yml"), YamlFileResourceOptions.builder().charset(StandardCharsets.UTF_8).indentationSize(2).build());

        builder.useDefaultMigrationService(); // use default migration service

        consumer.accept(builder); // overrides the default migration service if set in the consumer.

        this.config = builder.create();

        this.pluginManager = new FusionPluginManager();
        this.fileManager = new FileManager();
    }

    public abstract String parsePlaceholders(@NotNull final Audience audience, @NotNull final String message);

    public abstract ICommandManager getCommandManager();

    public abstract String chomp(@NotNull final String message);

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

    /**
     * Sends a log message parsed with MiniMessage.
     *
     * @param type      the name of the logger level
     * @param message   the message to send
     * @param throwable the throwable
     */
    public void log(@NotNull final String type, @NotNull final String message, @NotNull final Throwable throwable) {
        if (!this.isVerbose()) return;

        final Component component = AdvUtils.parse(message);

        switch (type) {
            case "info" -> this.logger.info(component, throwable);
            case "error" -> this.logger.error(component, throwable);
            case "warn" -> this.logger.warn(component, throwable);
        }
    }

    /**
     * Sends a log message parsed with MiniMessage.
     *
     * @param type    the name of the logger level
     * @param message the message to send
     * @param args    the args
     */
    public void log(@NotNull final String type, @NotNull final String message, @NotNull final Object... args) {
        if (!this.isVerbose()) return;

        final Component component = AdvUtils.parse(message);

        switch (type) {
            case "info" -> this.logger.info(component, args);
            case "error" -> this.logger.error(component, args);
            case "warn" -> this.logger.warn(component, args);
        }
    }

    public void enable() {

    }

    public void reload() {
        this.config.reload();
    }

    public void disable() {
        FusionProvider.unregister();
    }

    public @NotNull final FusionPluginManager getPluginManager() {
        return this.pluginManager;
    }

    public @NotNull final ComponentLogger getLogger() {
        return this.logger;
    }

    public @NotNull final FileManager getFileManager() {
        return this.fileManager;
    }

    public @NotNull final String getRoundingFormat() {
        return this.config.getProperty(ConfigKeys.rounding_format);
    }

    public @NotNull final String getNumberFormat() {
        return this.config.getProperty(ConfigKeys.number_format);
    }

    public boolean isVerbose() {
        return this.config.getProperty(ConfigKeys.is_verbose);
    }

    public int getDepth() {
        return this.config.getProperty(ConfigKeys.recursion_depth);
    }

    public @NotNull final Path getPath() {
        return this.path;
    }
}