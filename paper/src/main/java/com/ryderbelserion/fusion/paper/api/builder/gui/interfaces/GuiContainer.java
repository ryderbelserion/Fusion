package com.ryderbelserion.fusion.paper.api.builder.gui.interfaces;

import com.ryderbelserion.fusion.paper.api.builder.gui.enums.GuiType;
import com.ryderbelserion.fusion.paper.api.builder.gui.objects.GuiProvider;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public interface GuiContainer {

    @NotNull String title();

    void title(@NotNull final String title);

    @NotNull Inventory createInventory(@NotNull final InventoryHolder inventoryHolder);

    @NotNull GuiType guiType();

    int inventorySize();

    int rows();

    class Chest implements GuiContainer {

        private final GuiProvider.Chest guiProvider;

        private int rows;
        private String title;

        public Chest(
            @NotNull final String title,
            @NotNull final GuiProvider.Chest guiProvider,
            final int rows
        ) {
            this.guiProvider = guiProvider;
            this.title = title;
            this.rows = rows;
        }

        @Override
        public @NotNull String title() {
            return title;
        }

        @Override
        public void title(@NotNull final String title) {
            this.title = title;
        }

        @Override
        public int inventorySize() {
            return this.rows * 9;
        }

        @Override
        public @NotNull GuiType guiType() {
            return GuiType.CHEST;
        }

        @Override
        public int rows() {
            return this.rows;
        }

        public void rows(final int rows) {
            this.rows = rows;
        }

        @Override
        public @NotNull Inventory createInventory(@NotNull final InventoryHolder inventoryHolder) {
            return this.guiProvider.getInventory(this.title, inventoryHolder, inventorySize());
        }
    }

    class Typed implements GuiContainer {

        private final GuiProvider.Typed guiProvider;
        private final GuiType guiType;
        private String title;

        public Typed(
            @NotNull final String title,
            @NotNull final GuiProvider.Typed guiProvider,
            @NotNull final GuiType guiType
        ) {
            this.guiProvider = guiProvider;
            this.title = title;
            this.guiType = guiType;
        }

        @Override
        public @NotNull String title() {
            return this.title;
        }

        @Override
        public void title(@NotNull String title) {
            this.title = title;
        }

        @Override
        public int inventorySize() {
            return this.guiType.getLimit();
        }

        @Override
        public @NotNull GuiType guiType() {
            return this.guiType;
        }

        @Override
        public int rows() {
            return 1;
        }

        @Override
        public @NotNull Inventory createInventory(@NotNull InventoryHolder inventoryHolder) {
            return this.guiProvider.getInventory(this.title, inventoryHolder, this.guiType.getInventoryType());
        }
    }
}