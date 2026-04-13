package com.ryderbelserion.fusion.paper.builders.gui.objects;

import com.ryderbelserion.fusion.paper.builders.gui.interfaces.GuiAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;

public class GuiItem {

    private GuiAction<InventoryClickEvent> action;
    private ItemStack itemStack;
    private int slot;

    public GuiItem(@NotNull final ItemType itemType, final int amount, @NotNull final GuiAction<InventoryClickEvent> action) {
        this(itemType.createItemStack(amount), action);
    }

    public GuiItem(@NotNull final ItemType itemType, final int amount) {
        this(itemType, amount, _ -> {});
    }

    public GuiItem(@NotNull final ItemStack itemStack, @NotNull final GuiAction<InventoryClickEvent> action) {
        this.itemStack = itemStack;
        this.action = action;
    }

    public GuiItem(@NotNull final ItemStack itemStack) {
        this(itemStack, _ -> {});
    }

    public @NotNull final GuiAction<InventoryClickEvent> getAction() {
        return this.action;
    }

    public void setAction(@NotNull final GuiAction<InventoryClickEvent> action) {
        this.action = action;
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

    public static GuiItem of(@NotNull final ItemType itemType, final int amount, @NotNull final GuiAction<InventoryClickEvent> action) {
        return new GuiItem(itemType, amount, action);
    }

    public static GuiItem of(@NotNull final ItemType itemType, final int amount) {
        return of(itemType, amount, _ -> {});
    }

    public static GuiItem of(@NotNull final ItemType itemType, @NotNull final GuiAction<InventoryClickEvent> action) {
        return new GuiItem(itemType, 1, action);
    }

    public static GuiItem of(@NotNull final ItemType itemType) {
        return of(itemType, 1);
    }
}