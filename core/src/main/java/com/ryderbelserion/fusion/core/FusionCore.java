package com.ryderbelserion.fusion.core;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.core.api.ConfigKeys;
import com.ryderbelserion.fusion.core.api.registry.types.FileRegistry;
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

    protected final FileManager fileManager;
    protected final SettingsManager config;

    protected final ComponentLogger logger;
    protected final Path dataPath;

    protected final FileRegistry registry;

    public FusionCore(@NotNull final Consumer<SettingsManagerBuilder> settings, @NotNull final YamlFileResourceOptions options, @NotNull final ComponentLogger logger, @NotNull final Path dataPath) {
        this.dataPath = dataPath;
        this.logger = logger;

        final SettingsManagerBuilder builder = SettingsManagerBuilder.withYamlFile(getDataPath().resolve("fusion.yml"), options);

        settings.accept(builder);

        this.config = builder.create();

        this.registry = new FileRegistry();

        this.fileManager = new FileManager(this.registry);
    }

    public FusionCore(@NotNull final ComponentLogger logger, @NotNull final Path dataPath) {
        this(consumer -> consumer.configurationData(ConfigKeys.class), YamlFileResourceOptions.builder().indentationSize(2).build(), logger, dataPath);
    }

    public abstract @NotNull Component placeholders(@NotNull final String line);

    public abstract @NotNull Component placeholders(@NotNull final String line, @NotNull final Map<String, String> placeholders);

    public abstract @NotNull Component placeholders(@Nullable final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders, @Nullable final List<TagResolver> tags);

    public abstract @NotNull Component placeholders(@Nullable final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders);

    public abstract @NotNull Component color(@NotNull final String line);

    public abstract @NotNull Component color(@NotNull final String line, @NotNull final Map<String, String> placeholders);

    public abstract @NotNull Component color(@Nullable final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders);

    public abstract void sendMessage(@NotNull final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders);

    public abstract void sendMessage(@NotNull final Audience audience, @NotNull final List<String> lines, @NotNull final Map<String, String> placeholders);

    public abstract @Nullable <T> T getHeadDatabaseAPI();

    public abstract @NotNull String getItemsPlugin();

    public String getRoundingFormat() {
        return this.config.getProperty(ConfigKeys.rounding_format);
    }

    public int getRecursionDepth() {
        return this.config.getProperty(ConfigKeys.recursion_depth);
    }

    public String getNumberFormat() {
        return this.config.getProperty(ConfigKeys.number_format);
    }

    public ComponentLogger getLogger() {
        return this.logger;
    }

    public Path getDataPath() {
        return this.dataPath;
    }

    public boolean isVerbose() {
        return this.config.getProperty(ConfigKeys.is_verbose);
    }

    public void reload() {
        this.config.reload();
    }

    public static class Provider {
        private static FusionCore core = null;

        public static void register(final FusionCore core) {
            Provider.core = core;
        }

        public static FusionCore get() {
            return core;
        }
    }
}