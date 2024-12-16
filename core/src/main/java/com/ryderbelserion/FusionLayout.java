package com.ryderbelserion;

import com.ryderbelserion.api.exception.FusionException;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class FusionLayout {

    private CommentedConfigurationNode config;
    private YamlConfigurationLoader loader;

    public abstract File getDataFolder();

    public abstract ComponentLogger getLogger();

    public abstract @NotNull String placeholders(@NotNull final String line);

    public abstract @NotNull String placeholders(@NotNull final String line, @NotNull final Map<String, String> placeholders);

    public abstract @NotNull String placeholders(@Nullable final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders);

    public abstract @NotNull Component color(@NotNull final String line);

    public abstract @NotNull Component color(@NotNull final String line, @NotNull final Map<String, String> placeholders);

    public abstract @NotNull Component color(@Nullable final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders);

    public abstract void sendMessage(@NotNull final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders);

    public abstract void sendMessage(@NotNull final Audience audience, @NotNull final List<String> lines, @NotNull final Map<String, String> placeholders);

    public abstract String chomp(@NotNull final String message);

    public String getNumberFormat() {
        return this.config.node("settings", "number_format").getString("#,###.##");
    }

    public String getRounding() {
        return this.config.node("settings", "rounding").getString("half_even");
    }

    public boolean isVerbose() {
        return this.config.node("settings", "is_verbose").getBoolean(false);
    }

    public void onEnable() {
        FusionProvider.register(this);

        final File vital = new File(getDataFolder(), "vital.yml");

        final File fusion = new File(getDataFolder(), "fusion.yml");

        if (vital.renameTo(fusion)) {
            getLogger().warn("Detected old vital.yml, Renaming to fusion.yml");
        }

        this.loader = YamlConfigurationLoader.builder().indent(2).file(fusion).build();
    }

    public void onDisable() {

    }

    public void onReload() {
        this.config = CompletableFuture.supplyAsync(() -> {
            try {
                return this.loader.load();
            } catch (final ConfigurateException exception) {
                throw new FusionException("Failed to load fusion.yml", exception);
            }
        }).join();
    }
}