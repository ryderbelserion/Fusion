package com.ryderbelserion.fusion.core;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.core.api.ConfigKeys;
import com.ryderbelserion.fusion.core.managers.ModuleManager;
import com.ryderbelserion.fusion.core.managers.PluginExtension;
import com.ryderbelserion.fusion.core.managers.files.FileManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class FusionCore {

    protected final PluginExtension pluginExtension;
    protected final ModuleManager moduleManager;
    protected final FileManager fileManager;
    protected final SettingsManager config;

    protected final ComponentLogger logger;
    protected final Path dataPath;

    public FusionCore(@NotNull final Consumer<SettingsManagerBuilder> consumer, @NotNull final YamlFileResourceOptions options, @NotNull final ComponentLogger logger, @NotNull final Path dataPath) {
        Provider.register(this);

        this.dataPath = dataPath;
        this.logger = logger;

        final SettingsManagerBuilder builder = SettingsManagerBuilder.withYamlFile(this.dataPath.resolve("fusion.yml"), options);

        builder.useDefaultMigrationService();

        consumer.accept(builder); // overrides the default migration service if set in the consumer.

        this.config = builder.create();

        this.fileManager = new FileManager();

        this.pluginExtension = new PluginExtension();
        this.moduleManager = new ModuleManager();
    }

    public FusionCore(@NotNull final Consumer<SettingsManagerBuilder> consumer, @NotNull final ComponentLogger logger, @NotNull final Path dataPath) {
        this(consumer, YamlFileResourceOptions.builder().indentationSize(2).build(), logger, dataPath);
    }

    public FusionCore(@NotNull ComponentLogger logger, @NotNull Path dataPath) {
        this(consumer -> consumer.configurationData(ConfigKeys.class), YamlFileResourceOptions.builder().indentationSize(2).build(), logger, dataPath);
    }

    public @NotNull Component placeholders(@Nullable final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders, @Nullable final List<TagResolver> tags) {
        return Component.empty();
    }

    public @NotNull Component placeholders(@Nullable final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders) {
        return Component.empty();
    }

    public @NotNull Component placeholders(@NotNull final String line, @NotNull final Map<String, String> placeholders) {
        return Component.empty();
    }

    public @NotNull Component placeholders(@NotNull final String line) {
        return Component.empty();
    }

    public @NotNull Component color(@Nullable final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders) {
        return Component.empty();
    }

    public @NotNull Component color(@NotNull final String line, @NotNull final Map<String, String> placeholders) {
        return Component.empty();
    }

    public @NotNull Component color(@NotNull final String line) {
        return Component.empty();
    }

    public void sendMessage(@NotNull final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders) {
        audience.sendMessage(color(audience, line, placeholders));
    }

    public void sendMessage(@NotNull final Audience audience, @NotNull final List<String> lines, @NotNull final Map<String, String> placeholders) {
        for (final String line : lines) {
            sendMessage(audience, line, placeholders);
        }
    }

    public <E> void registerEvent(@NotNull final E event) {

    }

    public @NotNull PluginExtension getPluginExtension() {
        return this.pluginExtension;
    }

    public @NotNull ModuleManager getModuleManager() {
        return this.moduleManager;
    }

    public @NotNull FileManager getFileManager() {
        return this.fileManager;
    }

    public @NotNull ComponentLogger getLogger() {
        return this.logger;
    }

    public @NotNull Path getDataPath() {
        return this.dataPath;
    }

    public void reload() {
        this.config.reload();
    }

    public void disable() {
        Provider.unregister();
    }

    public @Nullable<T> T getHeadDatabaseAPI() {
        return null;
    }

    public @NotNull String getItemsPlugin() {
        return "none";
    }

    public @NotNull String chomp(@NotNull final String line) {
        return "";
    }

    public @NotNull String getRoundingFormat() {
        return this.config.getProperty(ConfigKeys.rounding_format);
    }

    public int getRecursionDepth() {
        return this.config.getProperty(ConfigKeys.recursion_depth);
    }

    public @NotNull String getNumberFormat() {
        return this.config.getProperty(ConfigKeys.number_format);
    }

    public boolean isVerbose() {
        return this.config.getProperty(ConfigKeys.is_verbose);
    }

    public static class Provider {
        private static FusionCore core = null;

        public static void register(@NotNull final FusionCore core) {
            Provider.core = core;
        }

        public static void unregister() {
            Provider.core = null;
        }

        public static FusionCore get() {
            return core;
        }
    }
}