package com.ryderbelserion.fusion.paper.api.commands;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.ryderbelserion.fusion.core.api.commands.Command;
import com.ryderbelserion.fusion.core.utils.AdvUtils;
import com.ryderbelserion.fusion.paper.api.commands.context.PaperCommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class PaperCommand extends Command<CommandSourceStack, PaperCommandContext> {

    public PaperCommand() {}

    @Override
    public @NotNull CompletableFuture<Suggestions> suggestStringArgument(@NotNull final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip) {
        for (int count = min; count <= max; ++count) {
            final String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

            if (tooltip.isBlank()) {
                builder.suggest(uuid);
            } else {
                builder.suggest(uuid, MessageComponentSerializer.message().serialize(AdvUtils.parse(tooltip)));
            }
        }

        return builder.buildFuture();
    }

    @Override
    public @NotNull CompletableFuture<Suggestions> suggestStringArgument(@NotNull final SuggestionsBuilder builder, @NotNull final String tooltip) {
        return suggestStringArgument(builder, 1, 8, tooltip);
    }

    @Override
    public @NotNull CompletableFuture<Suggestions> suggestStringArgument(@NotNull final SuggestionsBuilder builder) {
        return suggestStringArgument(builder, "");
    }

    @Override
    public @NotNull CompletableFuture<Suggestions> suggestIntegerArgument(@NotNull final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip) {
        for (int count = min; count <= max; ++count) {
            if (tooltip.isBlank()) {
                builder.suggest(count);
            } else {
                builder.suggest(count, MessageComponentSerializer.message().serialize(AdvUtils.parse(tooltip)));
            }
        }

        return builder.buildFuture();
    }

    @Override
    public @NotNull CompletableFuture<Suggestions> suggestIntegerArgument(@NotNull final SuggestionsBuilder builder, @NotNull final String tooltip) {
        return suggestIntegerArgument(builder, 1, 64, tooltip);
    }

    @Override
    public @NotNull CompletableFuture<Suggestions> suggestIntegerArgument(@NotNull final SuggestionsBuilder builder) {
        return suggestIntegerArgument(builder, "");
    }

    @Override
    public @NotNull CompletableFuture<Suggestions> suggestDoubleArgument(@NotNull final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip) {
        int count = min;

        while (count <= max) {
            double x = count / 10.0;

            final String value = String.valueOf(x);

            if (tooltip.isBlank()) {
                builder.suggest(value);
            } else {
                builder.suggest(value, MessageComponentSerializer.message().serialize(AdvUtils.parse(tooltip)));
            }

            count++;
        }

        return builder.buildFuture();
    }

    @Override
    public @NotNull CompletableFuture<Suggestions> suggestDoubleArgument(@NotNull final SuggestionsBuilder builder, @NotNull final String tooltip) {
        return suggestDoubleArgument(builder, 0, 64, tooltip);
    }

    @Override
    public @NotNull CompletableFuture<Suggestions> suggestDoubleArgument(@NotNull final SuggestionsBuilder builder) {
        return suggestDoubleArgument(builder, "");
    }
}