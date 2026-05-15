package com.ryderbelserion.fusion.paper.builders.gui.objects;

import com.ryderbelserion.fusion.paper.builders.gui.interfaces.GuiAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.NonNull;

public class GuiItem {

    private GuiAction<InventoryClickEvent> action;
    private ItemStack itemStack;
    private int slot;

    public GuiItem(@NonNull final ItemType itemType, final int amount, @NonNull final GuiAction<InventoryClickEvent> action) {
        this(itemType.createItemStack(amount), action);
    }

    public GuiItem(@NonNull final ItemType itemType, final int amount) {
        this(itemType, amount, _ -> {});
    }

    public GuiItem(@NonNull final ItemStack itemStack, @NonNull final GuiAction<InventoryClickEvent> action) {
        this.itemStack = itemStack;
        this.action = action;
    }

    public GuiItem(@NonNull final ItemStack itemStack) {
        this(itemStack, _ -> {});
    }

    public @NonNull final GuiAction<InventoryClickEvent> getAction() {
        return this.action;
    }

    public void setAction(@NonNull final GuiAction<InventoryClickEvent> action) {
        this.action = action;
    }

    public @NonNull final ItemStack setItemStack(@NonNull final ItemStack itemStack) {
        return this.itemStack = itemStack;
    }

    public @NonNull final ItemStack getItemStack() {
        return this.itemStack;
    }

    public GuiItem setSlot(final int slot) {
        this.slot = slot;

        return this;
    }

    public int getSlot() {
        return this.slot;
    }

    public static GuiItem of(@NonNull final ItemType itemType, final int amount, @NonNull final GuiAction<InventoryClickEvent> action) {
        return new GuiItem(itemType, amount, action);
    }

    public static GuiItem of(@NonNull final ItemType itemType, final int amount) {
        return of(itemType, amount, _ -> {});
    }

    public static GuiItem of(@NonNull final ItemType itemType, @NonNull final GuiAction<InventoryClickEvent> action) {
        return new GuiItem(itemType, 1, action);
    }

    public static GuiItem of(@NonNull final ItemType itemType) {
        return of(itemType, 1);
    }
}