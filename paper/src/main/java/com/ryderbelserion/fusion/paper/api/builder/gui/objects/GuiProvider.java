package com.ryderbelserion.fusion.paper.api.builder.gui.objects;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public final class GuiProvider {

    @FunctionalInterface
    public interface Chest {

        @NotNull Inventory getInventory(
                @NotNull final String title,
                @NotNull final InventoryHolder owner,
                final int rows
        );
    }

    @FunctionalInterface
    public interface Typed {

        @NotNull Inventory getInventory(
                @NotNull final String title,
                @NotNull final InventoryHolder owner,
                @NotNull final InventoryType inventoryType
        );
    }
}