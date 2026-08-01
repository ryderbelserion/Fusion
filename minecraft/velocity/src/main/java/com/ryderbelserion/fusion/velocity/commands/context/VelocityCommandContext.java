package com.ryderbelserion.fusion.velocity.commands.context;

import com.mojang.brigadier.context.CommandContext;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.mojang.context.AbstractCommandContext;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import org.jspecify.annotations.NullMarked;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
@NullMarked
public class VelocityCommandContext extends AbstractCommandContext<CommandSource> {

    public VelocityCommandContext(final CommandContext<CommandSource> context) {
        super(context);
    }

    public final CommandSource getSender() {
        return getContext().getSource();
    }

    public final Player getPlayer() {
        if (!isPlayer()) {
            throw new FusionException("This method can only be used for Players!");
        }

        return (Player) getSender();
    }

    public final boolean isPlayer() {
        return getSender() instanceof Player;
    }
}