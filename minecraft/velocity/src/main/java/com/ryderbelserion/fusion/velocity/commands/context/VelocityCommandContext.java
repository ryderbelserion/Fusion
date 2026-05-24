package com.ryderbelserion.fusion.velocity.commands.context;

import com.mojang.brigadier.context.CommandContext;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.mojang.context.AbstractCommandContext;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class VelocityCommandContext extends AbstractCommandContext<CommandSource> {

    public VelocityCommandContext(@NotNull final CommandContext<CommandSource> context) {
        super(context);
    }

    public @NotNull final CommandSource getSender() {
        return getContext().getSource();
    }

    public @NotNull final Player getPlayer() {
        if (!isPlayer()) {
            throw new FusionException("This method can only be used for Players!");
        }

        return (Player) getSender();
    }

    public final boolean isPlayer() {
        return getSender() instanceof Player;
    }
}