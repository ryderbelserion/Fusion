package com.ryderbelserion.fusion.kyori.commands;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.api.FusionProvider;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.kyori.commands.context.AbstractCommandContext;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.ryderbelserion.fusion.kyori.commands.serializers.MessageComponentSerializer;
import org.jspecify.annotations.NullMarked;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@NullMarked
public abstract class AbstractCommand<C, S, I extends AbstractCommandContext<S>> {

    private final FusionKyori<S> fusion = (FusionKyori<S>) FusionProvider.api();

    public CompletableFuture<Suggestions> suggestDoubleArgument(
            final SuggestionsBuilder builder,
            final String tooltip,
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

    public CompletableFuture<Suggestions> suggestIntegerArgument(
            final SuggestionsBuilder builder,
            final String tooltip,
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

    public CompletableFuture<Suggestions> suggestStringArgument(
            final SuggestionsBuilder builder,
            final String tooltip,
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

    public CompletableFuture<Suggestions> suggestArgument(
            final SuggestionsBuilder builder
    ) {
        return builder.buildFuture();
    }

    public abstract List<PermissionContext> getPermissions();

    public C registerPermissions() {
        getPermissions().forEach(this.fusion::registerPermission);

        return (C) this;
    }

    public abstract boolean requirement(final S context);

    public abstract LiteralCommandNode<S> literal();

    public abstract void run(final I context);

    public List<String> getAliases() {
        return List.of();
    }

    public String getDescription() {
        return "";
    }
}