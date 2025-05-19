package com.ryderbelserion.fusion.adventure;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FusionCore {

    public abstract String parsePlaceholders(final Audience audience, final String message);

    public abstract String chomp(final String message);

    public @NotNull Component color(@Nullable final Audience audience, final String message, @NotNull final Map<String, String> placeholders, @NotNull final List<TagResolver> tags) {
        final List<TagResolver> resolvers = new ArrayList<>(tags);

        placeholders.forEach((placeholder, value) -> resolvers.add(Placeholder.parsed(replaceBrackets(placeholder.replaceAll("<", "").replaceAll(">", "")).toLowerCase(), value)));

        return MiniMessage.miniMessage().deserialize(parsePlaceholders(audience, replaceBrackets(message)), TagResolver.resolver(resolvers));
    }

    public @NotNull Component color(@Nullable final Audience audience, final String message, @NotNull final Map<String, String> placeholders) {
        return color(audience, message, placeholders, List.of());
    }

    public @NotNull Component color(@Nullable final String message, @NotNull final Map<String, String> placeholders) {
        return color(null, message, placeholders);
    }

    public @NotNull Component color(@Nullable final String message) {
        return color(null, message, new HashMap<>());
    }

    private String replaceBrackets(final String input) {
        return input.replaceAll("\\{", "<").replaceAll("}", ">");
    }
}