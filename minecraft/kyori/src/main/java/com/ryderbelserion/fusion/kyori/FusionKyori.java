package com.ryderbelserion.fusion.kyori;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.kyori.mods.ModManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class FusionKyori extends FusionCore {

    private final ComponentLogger logger;
    private final ModManager modManager;

    public FusionKyori(@NotNull final Path path, @NotNull final ComponentLogger logger) {
        super(path);

        this.modManager = new ModManager();
        this.logger = logger;
    }

    public abstract Component parse(@NotNull final Audience audience, @NotNull final String message, @NotNull final Map<String, String> placeholders, @NotNull final List<TagResolver> tags);

    public abstract String papi(@NotNull final Audience audience, @NotNull final String message);

    @SuppressWarnings("DuplicatedCode")
    public void log(@NotNull final String type, @NotNull final String message, @NotNull final Throwable throwable) {
        if (!this.isVerbose()) return;

        final Component component = MiniMessage.miniMessage().deserialize(message);

        switch (type) {
            case "info" -> this.logger.info(component, throwable);
            case "error" -> this.logger.error(component, throwable);
            case "warn" -> this.logger.warn(component, throwable);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    public void log(@NotNull final String type, @NotNull final String message, @NotNull final Object... args) {
        if (!this.isVerbose()) return;

        final Component component = MiniMessage.miniMessage().deserialize(message);

        switch (type) {
            case "info" -> this.logger.info(component, args);
            case "error" -> this.logger.error(component, args);
            case "warn" -> this.logger.warn(component, args);
        }
    }

    public Component parse(@NotNull final Audience audience, @NotNull final String message, @NotNull final Map<String, String> placeholders) {
        return parse(audience, message, placeholders, List.of());
    }

    public <T> @Nullable T createProfile(@NotNull final UUID uuid, @Nullable final String name) {
        return null;
    }

    public Component parse(@NotNull final Audience audience, @NotNull final String message) {
        return parse(audience, message, new HashMap<>());
    }

    public Component parse(@NotNull final String message) {
        return parse(Audience.empty(), message, new HashMap<>());
    }

    public @NotNull final ComponentLogger getLogger() {
        return this.logger;
    }

    public @NotNull final ModManager getModManager() {
        return this.modManager;
    }

    public void init() {
        final Path dataPath = getDataPath();

        if (Files.notExists(dataPath)) {
            try {
                Files.createDirectory(dataPath);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}