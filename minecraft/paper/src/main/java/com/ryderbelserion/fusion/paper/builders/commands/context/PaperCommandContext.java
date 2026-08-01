package com.ryderbelserion.fusion.paper.builders.commands.context;

import com.mojang.brigadier.context.CommandContext;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.mojang.context.AbstractCommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperCommandContext extends AbstractCommandContext<CommandSourceStack> {

    public PaperCommandContext(final CommandContext<CommandSourceStack> context) {
        super(context);
    }

    public CommandSender getSender() {
        return getSource().getSender();
    }

    public Player getPlayer() {
        if (!isPlayer()) {
            throw new FusionException("This method can only be used for Players!");
        }

        return (Player) getSender();
    }

    public boolean isPlayer() {
        return getSender() instanceof Player;
    }
}