package com.ryderbelserion.fusion.commands;

import com.ryderbelserion.fusion.paper.api.builders.items.legacy.ItemBuilder;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;

public class ItemCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, String @NotNull [] args) {
        if (!(stack.getSender() instanceof Player player)) return;

        final ItemBuilder item = new ItemBuilder();

        player.getInventory().addItem(item.withType(ItemType.RED_TERRACOTTA).asItemStack());

        final com.ryderbelserion.fusion.paper.api.builders.items.ItemBuilder builder = com.ryderbelserion.fusion.paper.api.builders.items.ItemBuilder.from(ItemType.ITEM_FRAME).withType(ItemType.RED_BANNER);

        builder.addItemToInventory(player.getInventory());
    }
}