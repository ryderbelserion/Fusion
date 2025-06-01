package com.ryderbelserion.fusion.paper.api.builders.gui.interfaces;

import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

public enum GuiType {

    CHEST(InventoryType.CHEST, 9, 9),

    WORKBENCH(InventoryType.WORKBENCH, 9, 10),

    HOPPER(InventoryType.HOPPER, 5, 5),

    DISPENSER(InventoryType.DISPENSER, 8, 9),

    BREWING(InventoryType.BREWING, 4, 5);

    private final @NotNull InventoryType inventoryType;
    private final int limit;
    private final int fillSize;

    GuiType(@NotNull InventoryType inventoryType, int limit, int fillSize) {
        this.inventoryType = inventoryType;
        this.limit = limit;
        this.fillSize = fillSize;
    }

    public @NotNull InventoryType getInventoryType() {
        return this.inventoryType;
    }

    public int getFillSize() {
        return this.fillSize;
    }

    public int getLimit() {
        return this.limit;
    }
}