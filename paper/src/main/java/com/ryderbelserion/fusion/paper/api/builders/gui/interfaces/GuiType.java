package com.ryderbelserion.fusion.paper.api.builders.gui.interfaces;

import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

public enum GuiType {

    CHEST(InventoryType.CHEST, 9, 9),

    WORKBENCH(InventoryType.WORKBENCH, 9, 10),

    HOPPER(InventoryType.HOPPER, 5, 5),

    DISPENSER(InventoryType.DISPENSER, 8, 9),

    BREWING(InventoryType.BREWING, 4, 5);

    private @NotNull final InventoryType inventoryType;
    private final int limit;
    private final int fillSize;

    GuiType(@NotNull final InventoryType inventoryType, final int limit, final int fillSize) {
        this.inventoryType = inventoryType;
        this.limit = limit;
        this.fillSize = fillSize;
    }

    public @NotNull final InventoryType getInventoryType() {
        return this.inventoryType;
    }

    public final int getFillSize() {
        return this.fillSize;
    }

    public final int getLimit() {
        return this.limit;
    }
}