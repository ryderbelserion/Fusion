package com.ryderbelserion.fusion.kyori.commands.objects;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.UUID;

public abstract class AbstractCommand<S, D, P, A extends AbstractContext<S, P>> {

    public abstract void execute(@NotNull final A context);

    public abstract boolean requirement(@NotNull final S sender);

    public abstract @NotNull LiteralCommandNode<S> build();

    public abstract D getPermissionMode();

    public @NotNull List<String> getPermissions() {
        return List.of();
    }

    public abstract Suggestions supplyIntegers(@NotNull final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip);

    public abstract Suggestions supplyDoubles(@NotNull final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip);

    public abstract Suggestions supplyStrings(@NotNull final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip);

    public abstract Suggestions supplyIntegers(@NotNull final SuggestionsBuilder builder, @NotNull final String tooltip);

    public abstract Suggestions supplyDoubles(@NotNull final SuggestionsBuilder builder, @NotNull final String tooltip);

    public abstract Suggestions supplyStrings(@NotNull final SuggestionsBuilder builder, @NotNull final String tooltip);

}