package com.ryderbelserion.fusion.paper.api.commands.objects;

import com.mojang.brigadier.context.CommandContext;
import com.ryderbelserion.fusion.kyori.commands.objects.AbstractContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AbstractPaperContext extends AbstractContext<CommandSourceStack, Player> {

    public AbstractPaperContext(@NotNull final CommandContext context) {
        super(context);
    }

    public @NotNull final CommandSender getCommandSender() {
        return getSource().getSender();
    }

    @Override
    public @NotNull final Player getPlayer() {
        return (Player) getCommandSender();
    }

    @Override
    public final boolean isPlayer() {
        return getCommandSender() instanceof Player;
    }
}