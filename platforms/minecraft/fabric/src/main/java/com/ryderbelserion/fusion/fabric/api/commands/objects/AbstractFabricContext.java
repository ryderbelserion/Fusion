package com.ryderbelserion.fusion.fabric.api.commands.objects;

import com.mojang.brigadier.context.CommandContext;
import com.ryderbelserion.fusion.kyori.commands.objects.AbstractContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class AbstractFabricContext extends AbstractContext<CommandSourceStack, Player> {

    public AbstractFabricContext(@NotNull final CommandContext context) {
        super(context);
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