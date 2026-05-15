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
import org.jspecify.annotations.NonNull;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractCommand<C, S, I extends AbstractCommandContext<S>> {

    private final FusionKyori fusion = (FusionKyori) FusionProvider.getInstance();

    public @NonNull CompletableFuture<Suggestions> suggestDoubleArgument(
            @NonNull final SuggestionsBuilder builder,
            @NonNull final String tooltip,
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

    public @NonNull CompletableFuture<Suggestions> suggestIntegerArgument(
            @NonNull final SuggestionsBuilder builder,
            @NonNull final String tooltip,
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

    public @NonNull CompletableFuture<Suggestions> suggestStringArgument(
            @NonNull final SuggestionsBuilder builder,
            @NonNull final String tooltip,
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

    public @NonNull CompletableFuture<Suggestions> suggestArgument(
            @NonNull final SuggestionsBuilder builder
    ) {
        return builder.buildFuture();
    }

    public abstract @NonNull List<PermissionContext> getPermissions();

    public @NonNull C registerPermissions() {
        getPermissions().forEach(this.fusion::registerPermission);

        return (C) this;
    }

    public abstract boolean requirement(@NonNull final S context);

    public abstract @NonNull LiteralCommandNode<S> literal();

    public abstract void run(@NonNull final I context);

    public @NonNull List<String> getAliases() {
        return List.of();
    }

    public @NonNull String getDescription() {
        return "";
    }
}