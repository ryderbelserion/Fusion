package com.ryderbelserion.fusion.velocity.api.commands.objects;

import com.mojang.brigadier.context.CommandContext;
import com.ryderbelserion.fusion.common.api.commands.objects.ICommandContext;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.NotNull;

public class VelocityCommandContext extends ICommandContext<CommandSource, Player> {

    public VelocityCommandContext(@NotNull final CommandContext<CommandSource> context) {
        super(context);
    }

    @Override
    public final CommandSource getSource() {
        return super.getSource();
    }

    @Override
    public final Player getPlayer() {
        return (Player) getSource();
    }

    @Override
    public final boolean isPlayer() {
        return getSource() instanceof Player;
    }
}