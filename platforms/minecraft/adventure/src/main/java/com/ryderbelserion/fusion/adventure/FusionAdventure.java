package com.ryderbelserion.fusion.adventure;

import ch.jalu.configme.SettingsManagerBuilder;
import com.ryderbelserion.fusion.adventure.utils.AdvUtils;
import com.ryderbelserion.fusion.adventure.utils.StringUtils;
import com.ryderbelserion.fusion.core.FusionCore;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;

public abstract class FusionAdventure extends FusionCore {

    public FusionAdventure(@NotNull final Logger logger, @NotNull final Path path, @NotNull final Consumer<SettingsManagerBuilder> consumer) {
        super(logger, path, consumer);
    }

    public abstract String parsePlaceholders(@NotNull final Audience audience, @NotNull final String message);

    public abstract @NotNull String chomp(@NotNull final String message);

    public abstract Logger getLogger();

    public @NotNull Component color(@NotNull final Audience audience, @NotNull final String message, @NotNull final Map<String, String> placeholders, @NotNull final List<TagResolver> tags) {
        final List<TagResolver> resolvers = new ArrayList<>(tags);

        placeholders.forEach((placeholder, value) -> resolvers.add(Placeholder.parsed(StringUtils.replaceBrackets(placeholder).toLowerCase(), value)));

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