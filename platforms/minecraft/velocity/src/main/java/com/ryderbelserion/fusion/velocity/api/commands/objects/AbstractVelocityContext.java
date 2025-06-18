package com.ryderbelserion.fusion.velocity.api.commands.objects;

import com.mojang.brigadier.context.CommandContext;
import com.ryderbelserion.fusion.kyori.commands.objects.AbstractContext;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.NotNull;

public class AbstractVelocityContext extends AbstractContext<CommandSource, Player> {

    public AbstractVelocityContext(@NotNull final CommandContext<CommandSource> context) {
        super(context);
    }

    public @NotNull final CommandSource getCommandSender() {
        return getSource();
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