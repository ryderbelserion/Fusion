package com.ryderbelserion.fusion.kyori;

import com.ryderbelserion.fusion.core.FusionCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class FusionKyori<S> extends FusionCore {

    public FusionKyori(@NotNull final Path source, final Path path) {
        super(source, path);
    }

    public abstract String papi(@Nullable final S sender, @NotNull final String message);

    public @NotNull final Component asComponent(@NotNull final String message,
                                                @NotNull final Map<String, String> placeholders,
                                                @NotNull final List<TagResolver> tags
    ) {
        final List<TagResolver> resolvers = new ArrayList<>(tags);

        resolvers.add(TagResolver.standard());

        final MiniMessage builder = MiniMessage.builder()
                .tags(TagResolver.builder().resolvers(resolvers).build())
                .build();

        return builder.deserialize(replacePlaceholders(message, placeholders))
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    public @NotNull final Component asComponent(@Nullable final S sender,
                                          @NotNull final String message,
                                          @NotNull final Map<String, String> placeholders,
                                          @NotNull final List<TagResolver> tags
    ) {
        return asComponent(papi(sender, message), placeholders, tags);
    }

    public @NotNull final Component asComponent(@NotNull final S audience,
                           @NotNull final String message,
                           @NotNull final Map<String, String> placeholders
    ) {
        return asComponent(audience, message, placeholders, List.of());
    }

    public @NotNull final Component asComponent(@NotNull final S audience,
                           @NotNull final String message
    ) {
        return asComponent(audience, message, Map.of());
    }

    public @NotNull final Component asComponent(@NotNull final String message,
                           @NotNull final Map<String, String> placeholders
    ) {
        return asComponent(message, placeholders, List.of());
    }

    public @NotNull final Component asComponent(@NotNull final String message) {
        return asComponent(message, Map.of(), List.of());
    }
}