package com.ryderbelserion.fusion.kyori;

import com.ryderbelserion.fusion.kyori.commands.CommandManager;
import com.ryderbelserion.fusion.kyori.components.KyoriLogger;
import com.ryderbelserion.fusion.kyori.utils.AdvUtils;
import com.ryderbelserion.fusion.kyori.utils.StringUtils;
import com.ryderbelserion.fusion.core.FusionCore;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class FusionKyori extends FusionCore {

    protected final KyoriLogger logger;

    protected FusionKyori(@NotNull final ComponentLogger logger, @NotNull final Path path) {
        super(path);

        this.logger = new KyoriLogger(logger, this);
    }

    public abstract String parsePlaceholders(@NotNull final Audience audience, @NotNull final String message);

    public abstract @NotNull String chomp(@NotNull final String message);

    public abstract CommandManager getCommandManager();

    public boolean isPluginEnabled(final String name) {
        return false;
    }

    @Override
    public @NotNull final KyoriLogger getLogger() {
        return this.logger;
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
}