package com.ryderbelserion.fusion.kyori;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FusionKyori {

    public abstract Component parse(@NotNull final Audience audience, @NotNull final String message, @NotNull final Map<String, String> placeholders, @NotNull final List<TagResolver> tags);

    public abstract String papi(@NotNull final Audience audience, @NotNull final String message);

    public Component parse(@NotNull final Audience audience, @NotNull final String message, @NotNull final Map<String, String> placeholders) {
        return parse(audience, message, placeholders, List.of());
    }

    public Component parse(@NotNull final Audience audience, @NotNull final String message) {
        return parse(audience, message, new HashMap<>());
    }

    public Component parse(@NotNull final String message) {
        return parse(Audience.empty(), message, new HashMap<>());
    }
}