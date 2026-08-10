package com.ryderbelserion.fusion.kyori;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.ryderbelserion.fusion.kyori.registry.mods.ModRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NonNull;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class FusionKyori<S, F extends FileManager> extends FusionCore<S, F> {

    public FusionKyori(@NonNull final F fileManager, @NonNull final Path path) {
        super(fileManager, path);
    }

    private ModRegistry modRegistry;

    @Override
    public FusionCore init() {
        super.init();

        this.modRegistry = new ModRegistry();
        this.modRegistry.init();

        return this;
    }

    public @NonNull final String parse(
            @NonNull final S sender,
            @NonNull final String message,
            @NonNull final Map<String, String> placeholders
    ) {
        return replacePlaceholders(papi(sender, message), placeholders);
    }

    public @NonNull final String parse(
            @NonNull final S sender,
            @NonNull final String message
    ) {
        return parse(sender, message, Map.of());
    }

    public @NonNull final Component asComponent(
            @NonNull final String message,
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

    public @NonNull final Component asComponent(
            @NonNull final S sender,
            @NonNull final String message,
            @NonNull final Map<String, String> placeholders,
            @NonNull final List<TagResolver> tags
    ) {
        return asComponent(papi(sender, message), placeholders, tags);
    }

    public @NonNull final Component asComponent(
            @NonNull final S audience,
            @NonNull final String message,
            @NonNull final Map<String, String> placeholders
    ) {
        return asComponent(audience, message, placeholders, List.of());
    }

    public @NonNull final Component asComponent(
            @NonNull final S audience,
            @NonNull final String message
    ) {
        return asComponent(audience, message, Map.of());
    }

    public @NonNull final Component asComponent(
            @NonNull final String message,
            @NonNull final Map<String, String> placeholders
    ) {
        return asComponent(message, placeholders, List.of());
    }

    public @NonNull final Component asComponent(
            @NonNull final String message
    ) {
        return asComponent(message, Map.of(), List.of());
    }

    public void registerPermission(
            @NonNull final PermissionContext permission
    ) {

    }

    public @NonNull final ModRegistry getModRegistry() {
        return this.modRegistry;
    }
}