package com.ryderbelserion.fusion.mojang;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.mojang.context.AbstractCommandContext;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.ryderbelserion.fusion.mojang.serializers.MessageComponentSerializer;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractCommand<C, S, I extends AbstractCommandContext<S>> {

    private final FusionKyori fusion = (FusionKyori) FusionProvider.getInstance();

    public @NotNull CompletableFuture<Suggestions> suggestDoubleArgument(
            @NotNull final SuggestionsBuilder builder,
            @NotNull final String tooltip,
            final int minimum,
            final int maximum
    ) {
        final Message message = tooltip.isBlank() ? null : MessageComponentSerializer.message().serialize(this.fusion.asComponent(tooltip));

        final boolean isBuilt = message != null;

        for (int current = minimum; current <= maximum; ++current) {
            double origin = current / 10.0;

            if (isBuilt) {
                builder.suggest(String.valueOf(origin), message);

                continue;
            }

            builder.suggest(String.valueOf(origin));
        }

        return suggestArgument(builder);
    }

    public @NotNull CompletableFuture<Suggestions> suggestIntegerArgument(
            @NotNull final SuggestionsBuilder builder,
            @NotNull final String tooltip,
            final int minimum,
            final int maximum
    ) {
        final Message message = tooltip.isBlank() ? null : MessageComponentSerializer.message().serialize(this.fusion.asComponent(tooltip));

        final boolean isBuilt = message != null;

        for (int current = minimum; current <= minimum; ++current) {
            if (current >= maximum) {
                break;
            }

            if (isBuilt) {
                builder.suggest(current, message);

                continue;
            }

            builder.suggest(current);
        }

        return suggestArgument(builder);
    }

    public @NotNull CompletableFuture<Suggestions> suggestStringArgument(
            @NotNull final SuggestionsBuilder builder,
            @NotNull final String tooltip,
            final int minimum
    ) {
        final Message message = tooltip.isBlank() ? null : MessageComponentSerializer.message().serialize(this.fusion.asComponent(tooltip));

        final boolean isBuilt = message != null;

        for (int current = minimum; current <= minimum; ++current) {
            final String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

            if (isBuilt) {
                builder.suggest(uuid);

                continue;
            }

            builder.suggest(uuid, message);
        }

        return suggestArgument(builder);
    }

    public @NotNull CompletableFuture<Suggestions> suggestArgument(
            @NotNull final SuggestionsBuilder builder
    ) {
        return builder.buildFuture();
    }

    public abstract @NotNull List<PermissionContext> getPermissions();

    public @NotNull C registerPermissions() {
        getPermissions().forEach(this.fusion::registerPermission);

        return (C) this;
    }

    public abstract boolean requirement(@NotNull final S context);

    public abstract @NotNull LiteralCommandNode<S> literal();

    public abstract void run(@NotNull final I context);

    public @NotNull List<String> getAliases() {
        return List.of();
    }

    public @NotNull String getDescription() {
        return "";
    }
}