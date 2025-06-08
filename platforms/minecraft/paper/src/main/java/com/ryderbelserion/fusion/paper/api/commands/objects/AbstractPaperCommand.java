package com.ryderbelserion.fusion.paper.api.commands.objects;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.kyori.commands.objects.AbstractCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractPaperCommand extends AbstractCommand<CommandSourceStack, PermissionDefault, Player, AbstractPaperContext> {

    private final FusionKyori kyori = (FusionKyori) FusionCore.Provider.get();

    public @NotNull List<AbstractPaperCommand> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public @NotNull final Suggestions supplyIntegers(@NotNull final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip) {
        for (int count = min; count <= max; ++count) {
            if (tooltip.isEmpty()) {
                builder.suggest(count);
            } else {
                builder.suggest(count, MessageComponentSerializer.message().serialize(this.kyori.color(tooltip)));
            }
        }

        return builder.build();
    }

    @Override
    public @NotNull final Suggestions supplyDoubles(@NotNull final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip) {
        int count = min;

        while (count <= max) {
            if (tooltip.isEmpty()) {
                builder.suggest(String.valueOf(count / 10.0));
            } else {
                builder.suggest(String.valueOf(count / 10.0), MessageComponentSerializer.message().serialize(this.kyori.color(tooltip)));
            }

            count++;
        }

        return builder.build();
    }

    @Override
    public @NotNull final Suggestions supplyStrings(@NotNull final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip) {
        for (int count = min; count <= max; ++count) {
            final String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

            if (tooltip.isEmpty()) {
                builder.suggest(uuid);
            } else {
                builder.suggest(uuid, MessageComponentSerializer.message().serialize(this.kyori.color(tooltip)));
            }
        }

        return builder.build();
    }

    @Override
    public @NotNull final Suggestions supplyIntegers(@NotNull final SuggestionsBuilder builder, @NotNull final String tooltip) {
        return supplyIntegers(builder, 1, 64, tooltip);
    }

    @Override
    public @NotNull final Suggestions supplyDoubles(@NotNull final SuggestionsBuilder builder, @NotNull final String tooltip) {
        return supplyDoubles(builder, 0, 64, tooltip);
    }

    @Override
    public @NotNull final Suggestions supplyStrings(@NotNull final SuggestionsBuilder builder, @NotNull final String tooltip) {
        return supplyStrings(builder, 1, 8, tooltip);
    }
}