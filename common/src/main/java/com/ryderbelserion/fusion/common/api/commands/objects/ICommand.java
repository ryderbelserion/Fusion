package com.ryderbelserion.fusion.common.api.commands.objects;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public abstract class ICommand<S, L, P, A extends ICommandContext<S, P>> {

    public abstract void execute(@NotNull final A context);

    public abstract boolean requirement(@NotNull final S sender);

    public abstract @NotNull L build();

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