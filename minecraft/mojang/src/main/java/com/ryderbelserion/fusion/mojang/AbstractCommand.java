package com.ryderbelserion.fusion.mojang;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.mojang.context.AbstractCommandContext;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.ryderbelserion.fusion.mojang.enums.SuggestionType;
import com.ryderbelserion.fusion.mojang.serializers.MessageComponentSerializer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class AbstractCommand<C, S, I extends AbstractCommandContext<S>> {

    private final FusionKyori fusion = (FusionKyori) FusionProvider.getInstance();

    public @NotNull CompletableFuture<Suggestions> suggestArgument(
            @NotNull final SuggestionsBuilder builder,
            @NotNull final SuggestionType type,
            @NotNull final String tooltip,
            final int amount,
            final int ceiling
    ) {
        final boolean isBlank = tooltip.isBlank();
        final Message message = isBlank ? MessageComponentSerializer.message().serialize(Component.empty()) : MessageComponentSerializer.message().serialize(this.fusion.asComponent(tooltip));

        return suggestArgument(builder, consumer -> {
            switch (type) {
                case INTEGER_SUGGESTION -> {
                    for (int initial = amount; initial <= amount; ++initial) {
                        if (isBlank) {
                            consumer.suggest(initial);

                            continue;
                        }

                        consumer.suggest(initial, message);
                    }
                }

                case DOUBLE_SUGGESTION -> {
                    int minimum = ceiling;

                    while (minimum <= ceiling) {
                        final String value = String.valueOf(minimum / 10.0);

                        if (isBlank) {
                            builder.suggest(value);
                        } else {
                            builder.suggest(value, message);
                        }

                        minimum++;
                    }
                }

                case STRING_SUGGESTION -> {
                    for (int initial = amount; initial <= amount; ++initial) {
                        final String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

                        if (isBlank) {
                            consumer.suggest(uuid);

                            continue;
                        }

                        consumer.suggest(uuid, message);
                    }
                }
            }
        });
    }

    public @NotNull CompletableFuture<Suggestions> suggestArgument(
            @NotNull final SuggestionsBuilder builder,
            @NotNull final Collection<String> values,
            @NotNull final String tooltip
    ) {
        return suggestArgument(builder, consumer -> {
            final boolean isBlank = tooltip.isBlank();
            final Message message = isBlank ? MessageComponentSerializer.message().serialize(Component.empty()) : MessageComponentSerializer.message().serialize(this.fusion.asComponent(tooltip));

            for (final String value : values) {
                if (isBlank) {
                    consumer.suggest(value, message);

                    continue;
                }

                if (!value.isBlank()) {
                    consumer.suggest(value);
                }
            }
        });
    }

    public @NotNull CompletableFuture<Suggestions> suggestArgument(
            @NotNull final SuggestionsBuilder builder,
            @NotNull final Consumer<SuggestionsBuilder> consumer
    ) {
        consumer.accept(builder);

        return builder.buildFuture();
    }

    public abstract @NotNull List<PermissionContext> getPermissions();

    public @NotNull C registerPermissions() {
        getPermissions().forEach(this.fusion::registerPermission);

        return (C) this;
    }

    public abstract @NotNull LiteralCommandNode<S> literal();

    public abstract boolean requirement(@NotNull final S context);

    public abstract void run(@NotNull final I context);

}