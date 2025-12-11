package com.ryderbelserion.fusion.kyori;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.interfaces.permissions.enums.Mode;
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
import java.util.*;

public abstract class FusionKyori extends FusionCore {

    private final ComponentLogger logger;
    private final ModManager modManager;

    public FusionKyori(@NotNull final Path path, @NotNull final ComponentLogger logger) {
        super(path);

        this.modManager = new ModManager();
        this.logger = logger;
    }

    public abstract Component parse(@Nullable final Audience audience, @NotNull final String message, @NotNull final Map<String, String> placeholders, @NotNull final List<TagResolver> tags);

    public abstract Component parse(@NotNull final String message, @NotNull final Map<String, String> placeholders, @NotNull final List<TagResolver> tags);

    public Component parse(@NotNull final Audience audience, @NotNull final String message, @NotNull final Map<String, String> placeholders) {
        return parse(audience, message, placeholders, List.of());
    }

    public Component parse(@NotNull final Audience audience, @NotNull final String message) {
        return parse(audience, message, Map.of());
    }

    public Component parse(@NotNull final String message) {
        return parse(message, Map.of(), List.of());
    }

    public abstract String papi(@Nullable final Audience audience, @NotNull final String message);

    public abstract boolean hasPermission(@NotNull final Audience audience, @NotNull final String permission);

    public abstract void registerPermission(@NotNull final Mode mode, @NotNull final String parent, @NotNull final String description, @NotNull final Map<String, Boolean> children);

    public void registerPermission(@NotNull final Mode mode, @NotNull final String parent, @NotNull final String description) {
        registerPermission(mode, parent, description, Map.of());
    }

    public abstract void unregisterPermission(@NotNull final String parent);

    public abstract boolean isPermissionRegistered(@NotNull final String parent);

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

    public <T> @Nullable T createProfile(@NotNull final UUID uuid, @Nullable final String name) {
        return null;
    }

    public @NotNull final ComponentLogger getLogger() {
        return this.logger;
    }

    public @NotNull final ModManager getModManager() {
        return this.modManager;
    }

    public FusionKyori init() {
        final Path dataPath = getDataPath();

        if (Files.notExists(dataPath)) {
            try {
                Files.createDirectory(dataPath);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        return null;
    }
}