package com.ryderbelserion.fusion.core.api.commands;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.core.api.commands.context.CommandContext;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.CompletableFuture;

public abstract class Command<S, I extends CommandContext<S>> {

    public Command() {}

    public abstract void execute(I info);

    public abstract boolean requirement(S context);

    public abstract @NotNull String getPermission();

    public abstract @NotNull LiteralCommandNode<S> literal();

    public abstract @NotNull Command<S, I> registerPermission();

    public abstract @NotNull CompletableFuture<Suggestions> suggestStringArgument(final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip);

    public abstract @NotNull CompletableFuture<Suggestions> suggestStringArgument(final SuggestionsBuilder builder, @NotNull final String tooltip);

    public abstract @NotNull CompletableFuture<Suggestions> suggestStringArgument(final SuggestionsBuilder builder);

    public abstract @NotNull CompletableFuture<Suggestions> suggestIntegerArgument(final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip);

    public abstract @NotNull CompletableFuture<Suggestions> suggestIntegerArgument(final SuggestionsBuilder builder, @NotNull final String tooltip);

    public abstract @NotNull CompletableFuture<Suggestions> suggestIntegerArgument(final SuggestionsBuilder builder);

    public abstract @NotNull CompletableFuture<Suggestions> suggestDoubleArgument(final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip);

    public abstract @NotNull CompletableFuture<Suggestions> suggestDoubleArgument(final SuggestionsBuilder builder, @NotNull final String tooltip);

    public abstract @NotNull CompletableFuture<Suggestions> suggestDoubleArgument(final SuggestionsBuilder builder);

}