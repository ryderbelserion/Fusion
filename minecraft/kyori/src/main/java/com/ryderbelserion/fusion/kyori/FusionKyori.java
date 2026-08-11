package com.ryderbelserion.fusion.kyori;

import com.ryderbelserion.fusion.core.FusionCore;
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

public abstract class FusionKyori<S> extends FusionCore<S, Component, TagResolver> {

    public FusionKyori(@NonNull final Path path) {
        super(path);
    }

    private ModRegistry modRegistry;

    @Override
    public FusionCore init() {
        super.init();

        this.modRegistry = new ModRegistry();
        this.modRegistry.init();

        return this;
    }

    @Override
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

    public void registerPermission(@NonNull final PermissionContext permission) {

    }

    public @NonNull final ModRegistry getModRegistry() {
        return this.modRegistry;
    }
}