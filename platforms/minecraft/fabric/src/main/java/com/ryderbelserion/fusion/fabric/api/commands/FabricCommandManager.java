package com.ryderbelserion.fusion.fabric.api.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.kyori.commands.CommandManager;
import com.ryderbelserion.fusion.kyori.enums.PermissionMode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class FabricCommandManager extends CommandManager<CommandSourceStack> {

    @Override
    public void enable(@NotNull final LiteralCommandNode<CommandSourceStack> root, @NotNull final List<LiteralCommandNode<CommandSourceStack>> children) {
        CommandRegistrationCallback.EVENT.register((dispatcher, registry, environment) -> {
            dispatcher.register(Commands.literal("fabric")).addChild(children.getFirst());

            final LiteralCommandNode<CommandSourceStack> node = dispatcher.register(root.createBuilder());

            children.forEach(node::addChild);
        });
    }

    @Override
    public boolean hasPermission(@NotNull final CommandSourceStack stack, @NotNull final PermissionMode mode, @NotNull final String[] permissions) { //todo() implement permission support on fabric
        return false;
    }
}