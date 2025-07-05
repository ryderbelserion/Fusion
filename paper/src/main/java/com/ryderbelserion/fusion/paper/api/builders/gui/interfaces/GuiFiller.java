package com.ryderbelserion.fusion.paper.api.builders.gui.interfaces;

import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.paper.api.builders.gui.types.BaseGui;
import com.ryderbelserion.fusion.paper.api.builders.gui.types.PaginatedGui;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GuiFiller {

    private final BaseGui gui;

    public GuiFiller(@NotNull final BaseGui gui) {
        this.gui = gui;
    }

    public void fillTop(@NotNull final GuiItem guiItem) {
        fillTop(Collections.singletonList(guiItem));
    }

    public void fillTop(@NotNull final List<GuiItem> guiItems) {
        final List<GuiItem> items = repeatList(guiItems);

        for (int i = 0; i < 9; i++) {
            if (!this.gui.getGuiItems().containsKey(i)) this.gui.setItem(i, items.get(i));
        }
    }

    public void fillBottom(@NotNull final GuiItem guiItem) {
        fillBottom(Collections.singletonList(guiItem));
    }

    public void fillBottom(@NotNull final List<GuiItem> guiItems) {
        final int rows = this.gui.getRows();
        final List<GuiItem> items = repeatList(guiItems);

        for (int i = 9; i > 0; i--) {
            if (this.gui.getGuiItems().get((rows * 9) - i) == null) {
                this.gui.setItem((rows * 9) - i, items.get(i));
            }
        }
    }

    public void fillBorder(@NotNull final GuiItem guiItem) {
        fillBorder(Collections.singletonList(guiItem));
    }

    public void fillBorder(@NotNull final List<GuiItem> guiItems) {
        final int rows = this.gui.getRows();
        if (rows <= 2) return;

        final List<GuiItem> items = repeatList(guiItems);

        for (int i = 0; i < rows * 9; i++) {
            if ((i <= 8) || (i >= (rows * 9) - 8) && (i <= (rows * 9) - 2) || i % 9 == 0 || i % 9 == 8) {
                this.gui.setItem(i, items.get(i));
            }
        }
    }

    public void fillBetweenPoints(final int rowFrom, final int colFrom, final int rowTo, final int colTo, @NotNull final GuiItem guiItem) {
        fillBetweenPoints(rowFrom, colFrom, rowTo, colTo, Collections.singletonList(guiItem));
    }

    public void fillBetweenPoints(final int rowFrom, final int colFrom, final int rowTo, final int colTo, @NotNull final List<GuiItem> guiItems) {
        final int minRow = Math.min(rowFrom, rowTo);
        final int maxRow = Math.max(rowFrom, rowTo);
        final int minCol = Math.min(colFrom, colTo);
        final int maxCol = Math.max(colFrom, colTo);

        final int rows = this.gui.getRows();
        final List<GuiItem> items = repeatList(guiItems);

        for (int row = 1; row <= rows; row++) {
            for (int col = 1; col <= 9; col++) {
                final int slot = getSlotFromRowCol(row, col);

                if (!((row >= minRow && row <= maxRow) && (col >= minCol && col <= maxCol))) continue;

                this.gui.setItem(slot, items.get(slot));
            }
        }
    }

    public void fill(@NotNull final GuiItem guiItem) {
        fill(Collections.singletonList(guiItem));
    }

    public void fill(@NotNull final List<GuiItem> guiItems) {
        if (this.gui instanceof PaginatedGui) {
            throw new FusionException("Full filling a GUI is not supported in a Paginated GUI!");
        }

        final GuiType type = this.gui.getGuiType();

        final int fill = type == GuiType.CHEST ? this.gui.getRows() * type.getLimit() : type.getFillSize();

        final List<GuiItem> items = repeatList(guiItems);

        for (int i = 0; i < fill; i++) {
            if (this.gui.getGuiItems().get(i) == null) this.gui.setItem(i, items.get(i));
        }
    }

    public void fillSide(@NotNull final Side side, @NotNull final List<GuiItem> guiItems) {
        switch (side) {
            case LEFT:
                this.fillBetweenPoints(1, 1, this.gui.getRows(), 1, guiItems);
            case RIGHT:
                this.fillBetweenPoints(1, 9, this.gui.getRows(), 9, guiItems);
            case BOTH:
                this.fillSide(Side.LEFT, guiItems);
                this.fillSide(Side.RIGHT, guiItems);
        }
    }

    private @NotNull List<GuiItem> repeatList(@NotNull final List<GuiItem> guiItems) {
        final List<GuiItem> repeated = new ArrayList<>();

        Collections.nCopies(this.gui.getRows() * 9, guiItems).forEach(repeated::addAll);

        return repeated;
    }

    private int getSlotFromRowCol(final int row, final int col) {
        return (col + (row - 1) * 9) - 1;
    }

    public enum Side {
        LEFT,
        RIGHT,
        BOTH
    }
}