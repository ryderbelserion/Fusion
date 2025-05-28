package com.ryderbelserion.fusion.fabric.builders;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ItemBuilder {

    private final ItemStack itemStack;

    public ItemBuilder(ItemLike item) {
        this.itemStack = new ItemStack(item);
    }

    public ItemBuilder setAmount(final int amount) {
        this.itemStack.setCount(Math.min(amount, this.itemStack.getMaxStackSize()));

        return this;
    }

    public void addInventory(final Player player) {
        player.getInventory().addAndPickItem(this.itemStack);
    }
}