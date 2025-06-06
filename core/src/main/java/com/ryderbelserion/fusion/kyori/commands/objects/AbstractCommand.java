package com.ryderbelserion.fusion.kyori.commands.objects;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public abstract class AbstractCommand<S, P, A extends AbstractContext<S, P>> {

    public abstract void execute(A context);

    public abstract boolean requirement(S sender);

    public abstract @NotNull String getPermission();

    public abstract @NotNull LiteralCommandNode<S> literal();

    public abstract @NotNull AbstractCommand<S, P, A> registerPermission();

    public @NotNull final Suggestions supplyIntegers(@NotNull final SuggestionsBuilder builder, final int min, final int max) {
        for (int count = min; count <= max; ++count) {
            builder.suggest(count);
        }

        return builder.build();
    }

    public @NotNull final Suggestions supplyDoubles(@NotNull final SuggestionsBuilder builder, final int min, final int max) {
        int count = min;

        while (count <= max) {
            builder.suggest(String.valueOf(count / 10.0));

            count++;
        }

        return builder.build();
    }

    public @NotNull final Suggestions supplyStrings(@NotNull final SuggestionsBuilder builder, final int min, final int max) {
        for (int count = min; count <= max; ++count) {
            builder.suggest(UUID.randomUUID().toString().replace("-", "").substring(0, 8));
        }

        return builder.build();
    }

    public @NotNull final Suggestions supplyIntegers(@NotNull final SuggestionsBuilder builder) {
        return supplyIntegers(builder, 1, 64);
    }

    public @NotNull final Suggestions supplyDoubles(@NotNull final SuggestionsBuilder builder) {
        return supplyDoubles(builder, 0, 64);
    }

    public @NotNull final Suggestions supplyStrings(@NotNull final SuggestionsBuilder builder) {
        return supplyStrings(builder, 1, 8);
    }
}