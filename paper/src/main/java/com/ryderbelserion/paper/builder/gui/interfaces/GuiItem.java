package com.ryderbelserion.paper.builder.gui.interfaces;

import com.google.common.base.Preconditions;
import com.ryderbelserion.paper.builder.gui.types.GuiKeys;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

public class GuiItem {

    // Random UUID to identify the item when clicking
    private final UUID uuid = UUID.randomUUID();
    // Action to do when clicking on the item
    private GuiAction<InventoryClickEvent> action;
    // The ItemStack of the GuiItem
    private ItemStack itemStack;

    public GuiItem(@NotNull final ItemStack itemStack, @Nullable final GuiAction<@NotNull InventoryClickEvent> action) {
        Preconditions.checkNotNull(itemStack, "The ItemStack for the gui Item cannot be null!");

        if (action != null) {
            this.action = action;
        }

        // Sets the UUID to an NBT tag to be identifiable later
        setItemStack(itemStack);
    }

    public GuiItem(@NotNull final ItemStack itemStack) {
        this(itemStack, null);
    }

    public GuiItem(@NotNull final ItemType itemType) {
        this(itemType.createItemStack(), null);
    }

    public GuiItem(@NotNull final ItemType itemType, @Nullable final GuiAction<@NotNull InventoryClickEvent> action) {
        this(itemType.createItemStack(), action);
    }

    public @NotNull final ItemStack getItemStack() {
        return this.itemStack;
    }

    public void setItemStack(@NotNull final ItemStack itemStack) {
        Preconditions.checkNotNull(itemStack, "The ItemStack for the GUI Item cannot be null!");

        if (itemStack.getType() == Material.AIR) {
            this.itemStack = itemStack.clone();

            return;
        }

        final ItemStack item = itemStack.clone();

        item.editMeta(itemMeta -> {
            final PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();

            pdc.set(GuiKeys.key, PersistentDataType.STRING, uuid.toString());
        });

        this.itemStack = item;
    }

    public @NotNull final UUID getUuid() {
        return this.uuid;
    }

    public @Nullable final GuiAction<InventoryClickEvent> getAction() {
        return this.action;
    }

    public void setAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> action) {
        this.action = action;
    }
}