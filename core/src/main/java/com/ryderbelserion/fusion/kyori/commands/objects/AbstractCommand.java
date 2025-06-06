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

    public abstract LiteralCommandNode<S> build();

    public abstract void unregister();

    public abstract D getPermissionMode();

    public abstract @NotNull LiteralCommandNode<S> literal();

    public @NotNull List<String> getPermissions() {
        return List.of();
    }

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