package com.ryderbelserion.paper.commands;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.ryderbelserion.core.api.commands.Command;
import com.ryderbelserion.core.util.StringUtils;
import com.ryderbelserion.paper.commands.context.PaperCommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class PaperCommand extends Command<CommandSourceStack, PaperCommandContext> {

    public PaperCommand() {}

    @Override
    public @NotNull CompletableFuture<Suggestions> suggestStringArgument(final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip) {
        for (int count = min; count <= max; ++count) {
            final String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

            if (tooltip.isBlank()) {
                builder.suggest(uuid);
            } else {
                builder.suggest(uuid, MessageComponentSerializer.message().serialize(StringUtils.parse(tooltip)));
            }
        }

        return builder.buildFuture();
    }

    @Override
    public @NotNull CompletableFuture<Suggestions> suggestStringArgument(final SuggestionsBuilder builder, @NotNull final String tooltip) {
        return suggestStringArgument(builder, 1, 8, tooltip);
    }

    @Override
    public @NotNull CompletableFuture<Suggestions> suggestStringArgument(SuggestionsBuilder builder) {
        return suggestStringArgument(builder, "");
    }

    @Override
    public @NotNull CompletableFuture<Suggestions> suggestIntegerArgument(final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip) {
        for (int count = min; count <= max; ++count) {
            if (tooltip.isBlank()) {
                builder.suggest(count);
            } else {
                builder.suggest(count, MessageComponentSerializer.message().serialize(StringUtils.parse(tooltip)));
            }
        }

        return builder.buildFuture();
    }

    @Override
    public @NotNull CompletableFuture<Suggestions> suggestIntegerArgument(final SuggestionsBuilder builder, @NotNull final String tooltip) {
        return suggestIntegerArgument(builder, 1, 64, tooltip);
    }

    @Override
    public @NotNull CompletableFuture<Suggestions> suggestIntegerArgument(SuggestionsBuilder builder) {
        return suggestIntegerArgument(builder, "");
    }

    @Override
    public @NotNull CompletableFuture<Suggestions> suggestDoubleArgument(final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip) {
        int count = min;

        while (count <= max) {
            double x = count / 10.0;

            final String value = String.valueOf(x);

            if (tooltip.isBlank()) {
                builder.suggest(value);
            } else {
                builder.suggest(value, MessageComponentSerializer.message().serialize(StringUtils.parse(tooltip)));
            }

            count++;
        }

        return builder.buildFuture();
    }

    @Override
    public @NotNull CompletableFuture<Suggestions> suggestDoubleArgument(final SuggestionsBuilder builder, @NotNull final String tooltip) {
        return suggestDoubleArgument(builder, 0, 64, tooltip);
    }

    @Override
    public @NotNull CompletableFuture<Suggestions> suggestDoubleArgument(SuggestionsBuilder builder) {
        return suggestDoubleArgument(builder, "");
    }
}