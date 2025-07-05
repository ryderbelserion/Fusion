package com.ryderbelserion.fusion.fabric.api.commands.objects;

import com.mojang.brigadier.context.CommandContext;
import com.ryderbelserion.fusion.core.api.commands.objects.ICommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class FabricCommandContext extends ICommandContext<CommandSourceStack, Player> {

    public FabricCommandContext(@NotNull final CommandContext context) {
        super(context);
    }

    @Override
    public final CommandSourceStack getSource() {
        return super.getSource();
    }

    @Override
    public final Player getPlayer() {
        return getSource().getPlayer();
    }

    @Override
    public final boolean isPlayer() {
        return getSource().isPlayer();
    }
}