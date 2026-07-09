package com.ryderbelserion.fusion.kyori;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NonNull;
import org.jetbrains.annotations.Nullable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class FusionKyori<S, F extends FileManager> extends FusionCore<S, F> {

    public FusionKyori(@NonNull final F fileManager, @NonNull final Path path) {
        super(fileManager, path);
    }

    public @NonNull final String parse(@Nullable final S sender, @NonNull final String message, @NonNull final Map<String, String> placeholders) {
        return replacePlaceholders(papi(sender, message), placeholders);
    }

    public @NonNull final String parse(@Nullable final S sender, @NonNull final String message) {
        return parse(sender, message, Map.of());
    }

    public @NonNull final Component asComponent(@NonNull final String message,
                                                @NonNull final Map<String, String> placeholders,
                                                @NonNull final List<TagResolver> tags
    ) {
        final List<TagResolver> resolvers = new ArrayList<>(tags);

        resolvers.add(TagResolver.standard());

        final MiniMessage builder = MiniMessage.builder()
                .tags(TagResolver.builder().resolvers(resolvers).build())
                .build();

        return builder.deserialize(replacePlaceholders(message, placeholders))
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    public @NonNull final Component asComponent(@Nullable final S sender,
                                          @NonNull final String message,
                                          @NonNull final Map<String, String> placeholders,
                                          @NonNull final List<TagResolver> tags
    ) {
        return asComponent(papi(sender, message), placeholders, tags);
    }

    public @NonNull final Component asComponent(@NonNull final S audience,
                           @NonNull final String message,
                           @NonNull final Map<String, String> placeholders
    ) {
        return asComponent(audience, message, placeholders, List.of());
    }

    public @NonNull final Component asComponent(@NonNull final S audience,
                           @NonNull final String message
    ) {
        return asComponent(audience, message, Map.of());
    }

    public @NonNull final Component asComponent(@NonNull final String message,
                           @NonNull final Map<String, String> placeholders
    ) {
        return asComponent(message, placeholders, List.of());
    }

    public @NonNull final Component asComponent(@NonNull final String message) {
        return asComponent(message, Map.of(), List.of());
    }

    public void registerPermission(@NonNull final PermissionContext permission) {

    }
}