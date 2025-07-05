package com.ryderbelserion.fusion.paper.api.commands.objects;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.api.commands.objects.ICommand;
import com.ryderbelserion.fusion.paper.FusionPaper;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class PaperCommand extends ICommand<CommandSourceStack, LiteralCommandNode<CommandSourceStack>, Player, PaperCommandContext> {

    private final FusionPaper fusion = (FusionPaper) FusionProvider.get();

    public @NotNull List<PaperCommand> getChildren() {
        return new ArrayList<>();
    }

    public @NotNull PermissionDefault getPermissionDefault() {
        return PermissionDefault.OP;
    }

    @Override
    public @NotNull final Suggestions supplyIntegers(@NotNull final SuggestionsBuilder builder, final int min, final int max, @NotNull final String tooltip) {
        for (int count = min; count <= max; ++count) {
            if (tooltip.isEmpty()) {
                builder.suggest(count);
            } else {
                builder.suggest(count, MessageComponentSerializer.message().serialize(this.fusion.color(tooltip)));
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
                builder.suggest(String.valueOf(count / 10.0), MessageComponentSerializer.message().serialize(this.fusion.color(tooltip)));
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
                builder.suggest(uuid, MessageComponentSerializer.message().serialize(this.fusion.color(tooltip)));
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