package com.ryderbelserion.fusion.paper.builders.commands.context;

import com.mojang.brigadier.context.CommandContext;
import com.ryderbelserion.fusion.mojang.context.AbstractCommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PaperCommandContext extends AbstractCommandContext<CommandSourceStack> {

    public PaperCommandContext(@NotNull final CommandContext<CommandSourceStack> context) {
        super(context);
    }

    public @NotNull final CommandSender getSender() {
        return getSource().getSender();
    }

    public @NotNull final Player getPlayer() {
        return (Player) getSender();
    }

    public final boolean isPlayer() {
        return getSender() instanceof Player;
    }
}