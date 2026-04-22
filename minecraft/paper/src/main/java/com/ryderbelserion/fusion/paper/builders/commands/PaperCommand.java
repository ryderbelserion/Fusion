package com.ryderbelserion.fusion.paper.builders.commands;

import com.mojang.brigadier.context.CommandContext;
import com.ryderbelserion.fusion.mojang.AbstractCommand;
import com.ryderbelserion.fusion.paper.builders.commands.context.PaperCommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class PaperCommand extends AbstractCommand<PaperCommand, CommandSourceStack, PaperCommandContext> {

    public boolean hasPlayerArgument(@NotNull final CommandContext<CommandSourceStack> context, @NotNull final String name) {
        return hasArgument(context, name, Player.class);
    }

    public boolean hasItemArgument(@NotNull final CommandContext<CommandSourceStack> context, @NotNull final String name) {
        return hasArgument(context, name, ItemStack.class);
    }
}