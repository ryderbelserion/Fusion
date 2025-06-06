package com.ryderbelserion.fusion.fabric.api.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.fabric.api.commands.objects.AbstractFabricCommand;
import com.ryderbelserion.fusion.kyori.commands.CommandManager;
import com.ryderbelserion.fusion.kyori.enums.PermissionMode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.NotNull;

public class FabricCommandManager extends CommandManager<CommandSourceStack, AbstractFabricCommand> {

    @Override
    public void enable(@NotNull final AbstractFabricCommand command) {
        CommandRegistrationCallback.EVENT.register((dispatcher, registry, environment) -> {
            final LiteralCommandNode<CommandSourceStack> root = dispatcher.register(command.build().createBuilder());

            command.getChildren().forEach(node -> root.addChild(node.build()));
        });
    }

    @Override
    public boolean hasPermission(@NotNull final CommandSourceStack stack, @NotNull final PermissionMode mode, @NotNull final String[] permissions) { //todo() implement permission support on fabric
        return false;
    }
}