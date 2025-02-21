package com.ryderbelserion.fusion.core;

import com.ryderbelserion.fusion.core.api.exception.FusionException;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.util.FileUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
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
    private FileManager fileManager;

    public abstract File getDataFolder();

    public abstract ComponentLogger getLogger();

    public abstract @NotNull Component placeholders(@NotNull final String line);

    public abstract @NotNull Component placeholders(@NotNull final String line, @NotNull final Map<String, String> placeholders);

    public abstract @NotNull Component placeholders(@Nullable final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders, @Nullable final List<TagResolver> tags);

    public abstract @NotNull Component placeholders(@Nullable final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders);

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

    public String getItemPlugin() {
        return this.config.node("settings", "custom-items-plugin").getString("none");
    }

    public void enable(final String path) {
        FusionProvider.register(this);

        final File vital = new File(getDataFolder(), "vital.yml");

        final File fusion = new File(getDataFolder(), "fusion.yml");

        if (vital.renameTo(fusion)) {
            getLogger().warn("Detected old vital.yml, Renaming to fusion.yml");
        }

        FileUtils.saveResource(fusion.getName(), false, false);

        this.loader = YamlConfigurationLoader.builder().indent(2).file(fusion).build();

        reload();

        this.fileManager = new FileManager(path);
    }

    public void disable() {

    }

    public void reload() {
        this.config = CompletableFuture.supplyAsync(() -> {
            try {
                return this.loader.load();
            } catch (final ConfigurateException exception) {
                throw new FusionException("Failed to load fusion.yml", exception);
            }
        }).join();
    }

    public FileManager getFileManager() {
        return this.fileManager;
    }
}