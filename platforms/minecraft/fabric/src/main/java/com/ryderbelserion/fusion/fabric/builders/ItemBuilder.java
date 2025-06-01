package com.ryderbelserion.fusion.fabric.builders;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ItemBuilder {

    private final ItemStack itemStack;

    public ItemBuilder(ItemLike item) {
        this.itemStack = new ItemStack(item);
    }

    public ItemBuilder setAmount(int amount) {
        this.itemStack.setCount(Math.min(amount, this.itemStack.getMaxStackSize()));

        return this;
    }

    public ItemBuilder setName(String name) {
        this.itemStack.set(DataComponents.ITEM_NAME, Component.literal(name));

        return this;
    }

    public void addInventory(Player player) {
        player.getInventory().addAndPickItem(this.itemStack);
    }
}