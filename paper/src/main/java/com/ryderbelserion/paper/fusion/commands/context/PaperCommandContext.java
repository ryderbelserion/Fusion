package com.ryderbelserion.paper.fusion.commands.context;

import com.ryderbelserion.core.api.commands.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PaperCommandContext extends CommandContext<CommandSourceStack> {

    private final com.mojang.brigadier.context.CommandContext<CommandSourceStack> context;

    public PaperCommandContext(@NotNull final com.mojang.brigadier.context.CommandContext<CommandSourceStack> context) {
        super(context);

        this.context = context;
    }

    public @NotNull final CommandSender getCommandSender() {
        return getSource().getSender();
    }

    public @NotNull final Player getPlayer() {
        return (Player) getCommandSender();
    }

    public final boolean isPlayer() {
        return getCommandSender() instanceof Player;
    }

    public final com.mojang.brigadier.context.CommandContext<CommandSourceStack> getContext() {
        return this.context;
    }
}