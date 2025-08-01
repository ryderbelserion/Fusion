package com.ryderbelserion.fusion.velocity.api.commands.objects;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.v1.api.commands.objects.ICommand;
import com.ryderbelserion.fusion.velocity.FusionVelocity;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.VelocityBrigadierMessage;
import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class VelocityCommand extends ICommand<CommandSource, BrigadierCommand, Player, VelocityCommandContext> {

    private final FusionVelocity fusion = (FusionVelocity) FusionProvider.get();

    public @NotNull List<VelocityCommand> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public @NotNull final Suggestions supplyIntegers(@NotNull final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip) {
        for (int count = min; count <= max; ++count) {
            if (tooltip.isEmpty()) {
                builder.suggest(count);
            } else {
                builder.suggest(count, VelocityBrigadierMessage.tooltip(this.fusion.color(tooltip)));
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
                builder.suggest(String.valueOf(count / 10.0), VelocityBrigadierMessage.tooltip(this.fusion.color(tooltip)));
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
                builder.suggest(uuid, VelocityBrigadierMessage.tooltip(this.fusion.color(tooltip)));
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