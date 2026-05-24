package com.ryderbelserion.fusion.paper.builders.commands.context;

import com.mojang.brigadier.context.CommandContext;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.mojang.context.AbstractCommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class PaperCommandContext extends AbstractCommandContext<CommandSourceStack> {

    public PaperCommandContext(@NonNull final CommandContext<CommandSourceStack> context) {
        super(context);
    }

    public @NonNull final CommandSender getSender() {
        return getSource().getSender();
    }

    public @NonNull final Player getPlayer() {
        if (!isPlayer()) {
            throw new FusionException("This method can only be used for Players!");
        }

        return (Player) getSender();
    }

    public final boolean isPlayer() {
        return getSender() instanceof Player;
    }
}