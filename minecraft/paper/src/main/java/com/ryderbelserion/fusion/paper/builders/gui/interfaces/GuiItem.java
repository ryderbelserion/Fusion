package com.ryderbelserion.fusion.paper.builders.gui.interfaces;

import com.google.common.base.Preconditions;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class GuiItem {

    private GuiAction<InventoryClickEvent> action;
    private ItemStack itemStack;

    public GuiItem(@NotNull final ItemStack itemStack, @Nullable final GuiAction<@NotNull InventoryClickEvent> action) {
        Preconditions.checkNotNull(itemStack, "The ItemStack for the gui Item cannot be null!");

        if (action != null) {
            this.action = action;
        }

        this.itemStack = itemStack;
    }

    public GuiItem(@NotNull final ItemType itemType, @Nullable final GuiAction<@NotNull InventoryClickEvent> action) {
        this(itemType.createItemStack(), action);
    }

    public GuiItem(@NotNull final ItemStack itemStack) {
        this(itemStack, null);
    }

    public GuiItem(@NotNull final ItemType itemType) {
        this(itemType.createItemStack(), null);
    }

    public void setAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> action) {
        this.action = action;
    }

    public void setItemStack(@NotNull final ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public @Nullable GuiAction<InventoryClickEvent> getAction() {
        return this.action;
    }

    public @NotNull ItemStack getItemStack() {
        return this.itemStack;
    }
}