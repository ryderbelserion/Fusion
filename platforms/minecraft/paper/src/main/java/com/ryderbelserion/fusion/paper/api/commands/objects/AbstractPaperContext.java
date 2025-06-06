package com.ryderbelserion.fusion.paper.api.commands.objects;

import com.mojang.brigadier.context.CommandContext;
import com.ryderbelserion.fusion.kyori.commands.objects.AbstractContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AbstractPaperContext extends AbstractContext<CommandSourceStack, Player> {

    public AbstractPaperContext(@NotNull final CommandContext context) {
        super(context);
    }

    @Override
    public final Player getPlayer() {
        return (Player) getSource().getSender();
    }

    @Override
    public final boolean isPlayer() {
        return getSource().getSender() instanceof Player;
    }
}