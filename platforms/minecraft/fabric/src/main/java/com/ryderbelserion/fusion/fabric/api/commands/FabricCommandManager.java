package com.ryderbelserion.fusion.fabric.api.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.kyori.commands.CommandManager;
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
}