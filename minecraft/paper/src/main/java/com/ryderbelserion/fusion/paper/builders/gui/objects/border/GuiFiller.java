package com.ryderbelserion.fusion.paper.builders.gui.objects.border;

import com.ryderbelserion.fusion.paper.builders.gui.GuiBuilder;
import com.ryderbelserion.fusion.paper.builders.gui.enums.GuiBorder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GuiFiller {

    private final Inventory inventory;
    private final GuiBuilder builder;
    private final int size;

    public GuiFiller(@NotNull final GuiBuilder builder) {
        this.inventory = builder.getInventory();
        this.size = this.inventory.getSize();
        this.builder = builder;
    }

    public void fill(@NotNull final GuiBorder type, @NotNull final ItemStack itemStack) {
        fills(type, itemStack);
    }

    public void fills(@NotNull final GuiBorder type, @NotNull final ItemStack... items) {
        final List<ItemStack> sorted = sort(Arrays.asList(items));

        switch (type) {
            case TOP -> {
                for (int slot = 0; slot < 9; slot++) {
                    final ItemStack itemStack = this.inventory.getItem(slot);

                    if (itemStack == null) {
                        this.builder.addSlotAction(slot, sorted.get(slot));
                    }
                }
            }

            case BOTTOM -> {
                for (int slot = 9; slot > 0; slot--) {
                    final ItemStack itemStack = this.inventory.getItem(this.size - slot);

                    if (itemStack == null) {
                        this.builder.addSlotAction(this.size - slot, sorted.get(slot));
                    }
                }
            }

            case REMAINING_SLOTS -> {
                for (int slot = 0; slot < this.size; slot++) {
                    final ItemStack itemStack = this.inventory.getItem(slot);

                    if (itemStack != null) {
                        continue;
                    }

                    this.builder.addSlotAction(slot, sorted.get(slot));
                }
            }

            case BORDER -> {
                if (this.size <= 18) return;

                for (int slot = 0; slot < this.size; slot++) {
                    if ((slot <= 8) || (slot >= this.size - 8) && (slot <= this.size - 2) || slot % 9 == 0 || slot % 9 == 8) {
                        this.builder.addSlotAction(slot, sorted.get(slot));
                    }
                }
            }

            case LEFT_SIDE -> this.grids(1, 1, this.size, 1, items);
            case RIGHT_SIDE -> this.grids(1, 9, this.size, 9, items);

            case BOTH_SIDES -> {
                fills(GuiBorder.LEFT_SIDE, items);
                fills(GuiBorder.RIGHT_SIDE, items);
            }
        }
    }

    public void grid(final int rowFrom, final int colFrom, final int rowTo, final int colTo, @NotNull final ItemStack item) {
        grids(rowFrom, colFrom, rowTo, colTo, item);
    }

    public void grids(final int rowFrom, final int colFrom, final int rowTo, final int colTo, @NotNull final ItemStack... items) {
        final int minRow = Math.min(rowFrom, rowTo);
        final int maxRow = Math.max(rowFrom, rowTo);
        final int minCol = Math.min(colFrom, colTo);
        final int maxCol = Math.max(colFrom, colTo);

        final int rows = this.size / 9;
        final List<ItemStack> sorted = sort(Arrays.asList(items));

        for (int row = 1; row <= rows; row++) {
            for (int column = 1; column <= 9; column++) {
                final int slot = getSlotFromColumn(row, column);

                if (!((row >= minRow && row <= maxRow) && (column >= minCol && column <= maxCol))) continue;

                this.builder.addSlotAction(slot, sorted.get(slot));
            }
        }
    }

    private @NotNull List<ItemStack> sort(@NotNull final List<ItemStack> items) {
        final List<ItemStack> unsorted = new ArrayList<>();

        Collections.nCopies(this.size, items).forEach(unsorted::addAll);

        return unsorted;
    }

    private int getSlotFromColumn(final int row, final int column) {
        return (column + (row - 1) * 9) - 1;
    }
}