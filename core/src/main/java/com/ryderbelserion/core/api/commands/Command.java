package com.ryderbelserion.core.api.commands;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.core.api.commands.context.CommandInfo;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.CompletableFuture;

/**
 * Handles command execution per platform.
 * This class provides methods for executing commands, managing permissions, and suggesting command arguments.
 *
 * @param <S> the command source
 * @param <I> the command info
 *
 * @author Ryder Belserion
 * @version 0.17.0
 * @since 0.9.0
 */
public abstract class Command<S, I extends CommandInfo<S>> {

    /**
     * An empty constructor that does nothing.
     *
     * @since 0.9.0
     */
    public Command() {}

    /**
     * Executes the command with the given info.
     *
     * @param info the command info
     * @since 0.9.0
     */
    public abstract void execute(I info);

    /**
     * Gets the required permission to execute the command.
     *
     * @return the permission node
     * @since 0.9.0
     */
    public abstract @NotNull String getPermission();

    /**
     * Gets the command literal.
     *
     * @return the command literal
     * @since 0.9.0
     */
    public abstract @NotNull LiteralCommandNode<S> literal();

    /**
     * Registers the command permission.
     *
     * @return the command instance
     * @since 0.9.0
     */
    public abstract @NotNull Command<S, I> registerPermission();

    /**
     * Suggests UUIDs for command arguments.
     *
     * @param builder the suggestions builder
     * @param min the minimum number of suggestions
     * @param max the maximum number of suggestions
     * @param tooltip the tooltip
     * @return a list of suggestions
     * @since 0.9.0
     */
    public abstract @NotNull CompletableFuture<Suggestions> suggestStringArgument(final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip);

    /**
     * Suggests UUIDs for command arguments with default range.
     *
     * @param builder the suggestions builder
     * @param tooltip the tooltip
     * @return a list of suggestions
     * @since 0.9.0
     */
    public abstract @NotNull CompletableFuture<Suggestions> suggestStringArgument(final SuggestionsBuilder builder, @NotNull final String tooltip);

    /**
     * Suggests UUIDs for command arguments with default range.
     *
     * @param builder the suggestions builder
     * @return a list of suggestions
     * @since 0.9.0
     */
    public abstract @NotNull CompletableFuture<Suggestions> suggestStringArgument(final SuggestionsBuilder builder);

    /**
     * Suggests integers for command arguments.
     *
     * @param builder the suggestions builder
     * @param min the minimum number of suggestions
     * @param max the maximum number of suggestions
     * @param tooltip the tooltip
     * @return a list of suggestions
     * @since 0.9.0
     */
    public abstract @NotNull CompletableFuture<Suggestions> suggestIntegerArgument(final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip);

    /**
     * Suggests integers for command arguments with default range.
     *
     * @param builder the suggestions builder
     * @param tooltip the tooltip
     * @return a list of suggestions
     * @since 0.9.0
     */
    public abstract @NotNull CompletableFuture<Suggestions> suggestIntegerArgument(final SuggestionsBuilder builder, @NotNull final String tooltip);

    /**
     * Suggests integers for command arguments with default range.
     *
     * @param builder the suggestions builder
     * @return a list of suggestions
     * @since 0.9.0
     */
    public abstract @NotNull CompletableFuture<Suggestions> suggestIntegerArgument(final SuggestionsBuilder builder);

    /**
     * Suggests doubles for command arguments.
     *
     * @param builder the suggestions builder
     * @param min the minimum number of suggestions
     * @param max the maximum number of suggestions
     * @param tooltip the tooltip
     * @return a list of suggestions
     * @since 0.9.0
     */
    public abstract @NotNull CompletableFuture<Suggestions> suggestDoubleArgument(final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip);

    /**
     * Suggests doubles for command arguments with default range.
     *
     * @param builder the suggestions builder
     * @param tooltip the tooltip
     * @return a list of suggestions
     * @since 0.9.0
     */
    public abstract @NotNull CompletableFuture<Suggestions> suggestDoubleArgument(final SuggestionsBuilder builder, @NotNull final String tooltip);

    /**
     * Suggests doubles for command arguments with default range.
     *
     * @param builder the suggestions builder
     * @return a list of suggestions
     * @since 0.9.0
     */
    public abstract @NotNull CompletableFuture<Suggestions> suggestDoubleArgument(final SuggestionsBuilder builder);
}