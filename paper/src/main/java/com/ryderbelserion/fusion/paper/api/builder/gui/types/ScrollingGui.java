/**
 * MIT License
 * <p>
 * Copyright (c) 2021 TriumphTeam
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.ryderbelserion.fusion.paper.api.builder.gui.types;

import com.ryderbelserion.fusion.paper.api.builder.gui.enums.GuiComponent;
import com.ryderbelserion.fusion.paper.api.builder.gui.enums.GuiScrollType;
import com.ryderbelserion.fusion.paper.api.builder.gui.interfaces.GuiContainer;
import com.ryderbelserion.fusion.paper.api.builder.gui.objects.GuiItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class ScrollingGui extends PaginatedGui {

    private final GuiScrollType scrollType;
    private int scrollSize = 0;

    public ScrollingGui(final @NotNull GuiContainer guiContainer, final int pageSize, @NotNull final GuiScrollType scrollType, @NotNull final Set<GuiComponent> components) {
        super(guiContainer, pageSize, components);
        this.scrollType = scrollType;
    }

    @Override
    public boolean next() {
        final int current_page = getCurrentPageNumber();

        if (current_page * scrollSize + getPageSize() >= getCurrentPageItems().size() + scrollSize) return false;

        setPageNumber(current_page + 1);

        updatePage();

        return true;
    }

    @Override
    public boolean previous() {
        final int current_page = getCurrentPageNumber();

        if (current_page - 1 == 0) return false;

        setPageNumber(current_page - 1);

        updatePage();

        return true;
    }

    @Override
    public void open(@NotNull final Player player, final int openPage, @Nullable final Consumer<PaginatedGui> consumer) {
        if (player.isSleeping()) return;

        final Inventory inventory = getInventory();

        inventory.clear();

        if (this.scrollSize == 0) this.scrollSize = calculateScrollSize();

        if (openPage > 0 && (openPage * this.scrollSize + getPageSize() <= getCurrentPageItems().size() + this.scrollSize)) {
            setPageNumber(openPage);
        }

        populatePage();

        if (consumer != null) {
            consumer.accept(this);
        }

        player.openInventory(inventory);
    }

    @Override
    public void open(@NotNull final Player player, @NotNull final Consumer<PaginatedGui> consumer) {
        open(player, 1, consumer);
    }

    @Override
    public void open(@NotNull final Player player, final boolean purge) {
        open(player, 1, null);
    }

    @Override
    public void open(@NotNull final Player player) {
        open(player, 1, null);
    }

    /**
     * Overrides {@link PaginatedGui#updatePage()} to make it work with the specific scrolls
     */
    @Override
    public void updatePage() {
        clearPageContents();

        populatePage();
    }

    /**
     * Fills the page with the items
     */
    public void populatePage() {
        // Adds the paginated items to the page
        for (final GuiItem guiItem : getPage(getCurrentPageNumber())) {
            if (this.scrollType == GuiScrollType.HORIZONTAL) {
                putItemHorizontally(guiItem);

                continue;
            }

            putItemVertically(guiItem);
        }
    }

    /**
     * Calculates the size of each scroll
     *
     * @return The size of he scroll
     */
    private int calculateScrollSize() {
        int counter = 0;

        final Inventory inventory = getInventory();

        if (this.scrollType == GuiScrollType.VERTICAL) {
            boolean foundCol = false;

            for (int row = 1; row <= getRows(); row++) {
                for (int col = 1; col <= 9; col++) {
                    final int slot = getSlotFromRowColumn(row, col);

                    if (inventory.getItem(slot) == null) {
                        if (!foundCol) foundCol = true;

                        counter++;
                    }
                }

                if (foundCol) return counter;
            }

            return counter;
        }

        boolean foundRow = false;

        for (int col = 1; col <= 9; col++) {
            for (int row = 1; row <= getRows(); row++) {
                final int slot = getSlotFromRowColumn(row, col);

                if (inventory.getItem(slot) == null) {
                    if (!foundRow) foundRow = true;

                    counter++;
                }
            }

            if (foundRow) return counter;
        }

        return counter;
    }

    /**
     * Puts the item in the GUI for horizontal scrolling
     *
     * @param guiItem The gui item
     */
    private void putItemVertically(final GuiItem guiItem) {
        final Inventory inventory = getInventory();

        final ItemStack itemStack = guiItem.getItemStack();

        for (int slot = 0; slot < getRows() * 9; slot++) {
            if (getGuiItem(slot) != null || inventory.getItem(slot) != null) continue;

            getCurrentPageItems().put(slot, guiItem);

            inventory.setItem(slot, itemStack);

            break;
        }
    }

    /**
     * Puts item into the GUI for vertical scrolling
     *
     * @param guiItem The gui item
     */
    private void putItemHorizontally(final GuiItem guiItem) {
        final Inventory inventory = getInventory();

        final ItemStack itemStack = guiItem.getItemStack();

        for (int col = 1; col < 10; col++) {
            for (int row = 1; row <= getRows(); row++) {
                final int slot = getSlotFromRowColumn(row, col);

                if (getGuiItem(slot) != null || inventory.getItem(slot) != null) continue;

                getCurrentPageItems().put(slot, guiItem);

                inventory.setItem(slot, itemStack);

                return;
            }
        }
    }

    /**
     * Gets the items from the current page
     *
     * @param givenPage The page number
     * @return A list with all the items
     */
    private List<GuiItem> getPage(final int givenPage) {
        final int page = givenPage - 1;
        final int pageItemsSize = getCurrentPageItems().size();

        final List<GuiItem> guiPage = new ArrayList<>();

        int max = page * this.scrollSize + getPageSize();
        if (max > pageItemsSize) max = pageItemsSize;

        for (int i = page * this.scrollSize; i < max; i++) {
            guiPage.add(getCurrentPageItems().get(i));
        }

        return guiPage;
    }
}