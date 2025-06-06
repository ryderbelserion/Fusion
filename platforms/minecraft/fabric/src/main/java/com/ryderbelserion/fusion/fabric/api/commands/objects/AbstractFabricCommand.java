package com.ryderbelserion.fusion.fabric.api.commands.objects;

import com.ryderbelserion.fusion.kyori.commands.objects.AbstractCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFabricCommand extends AbstractCommand<CommandSourceStack, Player, AbstractFabricContext> {

    public @NotNull List<AbstractFabricCommand> getChildren() {
        return new ArrayList<>();
    }
}