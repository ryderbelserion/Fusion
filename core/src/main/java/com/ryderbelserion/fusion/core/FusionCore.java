package com.ryderbelserion.fusion.core;

import ch.jalu.configme.SettingsHolder;
import com.ryderbelserion.fusion.api.FusionApi;
import com.ryderbelserion.fusion.core.api.LoggerImpl;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public abstract class FusionCore extends FusionApi {

    private final LoggerImpl logger;
    private final Path dataFolder;

    public FusionCore(@NotNull final ComponentLogger logger, @NotNull final Path dataFolder) {
        this.logger = new LoggerImpl(logger);
        this.dataFolder = dataFolder;
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

    @Override
    public @NotNull final Path getDataFolder() {
        return this.dataFolder;
    }

    @Override
    public @NotNull final LoggerImpl getLogger() {
        return this.logger;
    }

    @SafeVarargs
    @Override
    public final void init(final String path, final Class<? extends SettingsHolder>... classes) {
        FusionProvider.register(this);

        super.init(path, classes);
    }

    public static class FusionProvider {
        private static FusionCore core = null;

        public static void register(final FusionCore core) {
            Provider.register(core);

            FusionProvider.core = core;
        }

        public static FusionCore get() {
            return core;
        }
    }
}