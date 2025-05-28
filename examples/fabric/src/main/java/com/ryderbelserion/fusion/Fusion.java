package com.ryderbelserion.fusion;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.RootCommandNode;
import com.ryderbelserion.fusion.fabric.builders.ItemBuilder;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class Fusion implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        CommandDispatcher<CommandSourceStack> dispatcher = new CommandDispatcher<>(new RootCommandNode<>());

        dispatcher.register(
                Commands.literal("fusion").executes(context -> {
                    final CommandSourceStack source = context.getSource();

                    if (source.isPlayer()) {
                        final ItemBuilder itemBuilder = new ItemBuilder(Items.ACACIA_BOAT).setAmount(32);

                        itemBuilder.addInventory(source.getPlayerOrException());

                        source.sendSystemMessage(Component.literal("Here is your fancy item!"));

                        return SINGLE_SUCCESS;
                    }

                    source.sendSystemMessage(Component.literal("This is the /fusion command for console!"));

                    return SINGLE_SUCCESS;
                })
        );
    }
}