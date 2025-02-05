package com.ryderbelserion.paper.commands;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.ryderbelserion.core.api.commands.Command;
import com.ryderbelserion.core.util.StringUtils;
import com.ryderbelserion.paper.commands.context.PaperCommandInfo;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Paper extension of Command.
 * Defines the structure for creating commands specific to the Paper server.
 *
 * @author Ryder Belserion
 * @version 0.15.0
 * @since 0.9.0
 */
public abstract class PaperCommand extends Command<CommandSourceStack, PaperCommandInfo> {

    /**
     * Constructs a PaperCommand with default settings.
     *
     * @since 0.9.0
     */
    public PaperCommand() {}

    /**
     * Suggests UUIDs for command arguments.
     *
     * @param builder {@inheritDoc}
     * @param min {@inheritDoc}
     * @param max {@inheritDoc}
     * @param tooltip {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.9.0
     */
    @Override
    public @NotNull CompletableFuture<Suggestions> suggestStringArgument(final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip) {
        for (int count = min; count <= max; ++count) {
            if (tooltip.isBlank()) {
                builder.suggest(UUID.randomUUID().toString().replace("-", "").substring(0, 8));
            } else {
                builder.suggest(UUID.randomUUID().toString().replace("-", "").substring(0, 8), MessageComponentSerializer.message().serialize(StringUtils.parse(tooltip)));
            }
        }

        return builder.buildFuture();
    }

    /**
     * {@inheritDoc}
     *
     * @param builder {@inheritDoc}
     * @param tooltip {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.9.0
     */
    @Override
    public @NotNull CompletableFuture<Suggestions> suggestStringArgument(final SuggestionsBuilder builder, @NotNull final String tooltip) {
        return suggestStringArgument(builder, 1, 8, tooltip);
    }

    /**
     * {@inheritDoc}
     *
     * @param builder {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.9.0
     */
    @Override
    public @NotNull CompletableFuture<Suggestions> suggestStringArgument(SuggestionsBuilder builder) {
        return suggestStringArgument(builder, "");
    }

    /**
     * {@inheritDoc}
     *
     * @param builder {@inheritDoc}
     * @param min {@inheritDoc}
     * @param max {@inheritDoc}
     * @param tooltip {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.9.0
     */
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

    /**
     * {@inheritDoc}
     *
     * @param builder {@inheritDoc}
     * @param tooltip {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.9.0
     */
    @Override
    public @NotNull CompletableFuture<Suggestions> suggestIntegerArgument(final SuggestionsBuilder builder, @NotNull final String tooltip) {
        return suggestIntegerArgument(builder, 1, 64, tooltip);
    }

    /**
     * {@inheritDoc}
     *
     * @param builder {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.9.0
     */
    @Override
    public @NotNull CompletableFuture<Suggestions> suggestIntegerArgument(SuggestionsBuilder builder) {
        return suggestIntegerArgument(builder, "");
    }

    /**
     * {@inheritDoc}
     *
     * @param builder {@inheritDoc}
     * @param min {@inheritDoc}
     * @param max {@inheritDoc}
     * @param tooltip {@inheritDoc} 
     * @return {@inheritDoc}
     * @since 0.9.0
     */
    @Override
    public @NotNull CompletableFuture<Suggestions> suggestDoubleArgument(final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip) {
        int count = min;

        while (count <= max) {
            double x = count / 10.0;

            if (tooltip.isBlank()) {
                builder.suggest(String.valueOf(x));
            } else {
                builder.suggest(String.valueOf(x), MessageComponentSerializer.message().serialize(StringUtils.parse(tooltip)));
            }

            count++;
        }

        return builder.buildFuture();
    }

    /**
     * {@inheritDoc}
     *
     * @param builder {@inheritDoc}
     * @param tooltip {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.9.0
     */
    @Override
    public @NotNull CompletableFuture<Suggestions> suggestDoubleArgument(final SuggestionsBuilder builder, @NotNull final String tooltip) {
        return suggestDoubleArgument(builder, 0, 64, tooltip);
    }

    /**
     * {@inheritDoc}
     *
     * @param builder {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.9.0
     */
    @Override
    public @NotNull CompletableFuture<Suggestions> suggestDoubleArgument(SuggestionsBuilder builder) {
        return suggestDoubleArgument(builder, "");
    }
}