package com.ryderbelserion.fusion.paper.builders.gui.objects;

import com.ryderbelserion.fusion.paper.builders.gui.interfaces.GuiAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GuiItem {

    private final GuiAction<InventoryClickEvent> action;
    private ItemStack itemStack;
    private int slot;

    public GuiItem(@NotNull final ItemStack itemStack, @NotNull final GuiAction<InventoryClickEvent> action) {
        this.itemStack = itemStack;
        this.action = action;
    }

    public @NotNull final GuiAction<InventoryClickEvent> getAction() {
        return this.action;
    }

    public @NotNull final ItemStack setItemStack(@NotNull final ItemStack itemStack) {
        return this.itemStack = itemStack;
    }

    public @NotNull final ItemStack getItemStack() {
        return this.itemStack;
    }

    public GuiItem setSlot(final int slot) {
        this.slot = slot;

        return this;
    }

    public int getSlot() {
        return this.slot;
    }
}