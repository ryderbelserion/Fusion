package com.ryderbelserion.fusion.fabric.api.commands.objects;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.common.api.commands.objects.ICommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public abstract class FabricCommand extends ICommand<CommandSourceStack, LiteralCommandNode<CommandSourceStack>, Player, FabricCommandContext> {

    public @NotNull List<FabricCommand> getChildren() {
        return new ArrayList<>();
    }
}