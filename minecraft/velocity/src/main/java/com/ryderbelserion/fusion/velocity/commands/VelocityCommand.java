package com.ryderbelserion.fusion.velocity.commands;

import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.kyori.commands.AbstractCommand;
import com.ryderbelserion.fusion.velocity.FusionVelocity;
import com.ryderbelserion.fusion.velocity.commands.context.VelocityCommandContext;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class VelocityCommand extends AbstractCommand<VelocityCommand, CommandSource, VelocityCommandContext> {

    private final FusionVelocity fusion = (FusionVelocity) FusionProvider.getInstance();

    public final BrigadierCommand getBrigadierCommand() {
        return new BrigadierCommand(literal());
    }
}