package com.ryderbelserion.fusion.builders;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ItemBuilder {

    private final ItemStack itemStack;

    public ItemBuilder(ItemLike item) {
        this.itemStack = new ItemStack(item);
    }

    public void addInventory(final Player player) {
        player.getInventory().addAndPickItem(this.itemStack);
    }
}