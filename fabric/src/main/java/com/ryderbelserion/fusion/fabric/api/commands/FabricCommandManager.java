package com.ryderbelserion.fusion.fabric.api.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.core.api.commands.ICommandManager;
import com.ryderbelserion.fusion.fabric.api.commands.objects.FabricCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class FabricCommandManager extends ICommandManager<CommandSourceStack, FabricCommand> {

    @Override
    public void enable(@NotNull final FabricCommand command, @Nullable final String description, @NotNull final List<String> aliases) {
        CommandRegistrationCallback.EVENT.register((dispatcher, registry, environment) -> {
            final LiteralCommandNode<CommandSourceStack> root = dispatcher.register(command.build().createBuilder());

            command.getChildren().forEach(node -> root.addChild(node.build()));
        });
    }

    @Override
    public void disable() {

    }
}