package com.ryderbelserion.fusion.paper.api.builder.gui.types;

import com.ryderbelserion.fusion.paper.api.builder.gui.objects.GuiItem;
import com.ryderbelserion.fusion.paper.api.builder.gui.interfaces.GuiContainer;
import com.ryderbelserion.fusion.paper.api.builder.gui.interfaces.types.IPaginatedGui;
import com.ryderbelserion.fusion.paper.api.builder.gui.enums.GuiComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class PaginatedGui extends BaseGui implements IPaginatedGui {

    private final List<GuiItem> pageItems = new ArrayList<>();
    private final Map<Integer, GuiItem> currentPage;

    private int pageNumber = 1;
    private int pageSize;

    public PaginatedGui(@NotNull final GuiContainer guiContainer, final int pageSize, @NotNull final Set<GuiComponent> components) {
        super(guiContainer, components);

        if (pageSize == 0) {
            calculatePageSize();
        } else {
            this.pageSize = pageSize;
        }

        final int size = guiContainer.inventorySize() * 9;

        this.currentPage = new LinkedHashMap<>(size);
    }

    @Override
    public final PaginatedGui setPageSize(final int pageSize) {
        this.pageSize = pageSize;

        return this;
    }

    @Override
    public void addItem(@NotNull final GuiItem guiItem) {
        this.pageItems.add(guiItem);
    }

    @Override
    public void addItem(@NotNull final GuiItem... items) {
        this.pageItems.addAll(Arrays.asList(items));
    }

    @Override
    public void removePageItem(final int slot) {
        if (!this.currentPage.containsKey(slot)) return;

        final GuiItem guiItem = this.currentPage.remove(slot);

        this.pageItems.remove(guiItem);

        updatePage();
    }

    @Override
    public void updatePageItem(final int slot, @NotNull final ItemStack itemStack) {
        if (!this.currentPage.containsKey(slot)) return;

        final GuiItem guiItem = this.currentPage.get(slot);

        guiItem.setItemStack(itemStack);

        getInventory().setItem(slot, guiItem.getItemStack());
    }

    @Override
    public void updatePageItem(final int row, final int col, @NotNull final ItemStack itemStack) {
        updatePageItem(getSlotFromRowColumn(row, col), itemStack);
    }

    @Override
    public void updatePageItem(final int row, final int col, @NotNull final GuiItem guiItem) {
        updatePageItem(getSlotFromRowColumn(row, col), guiItem);
    }

    @Override
    public void updatePageItem(final int slot, @NotNull final GuiItem guiItem) {
        if (!this.currentPage.containsKey(slot)) return;

        final int index = this.pageItems.indexOf(this.currentPage.get(slot));

        // Updates both lists and inventory
        this.currentPage.put(slot, guiItem);
        this.pageItems.set(index, guiItem);

        getInventory().setItem(slot, guiItem.getItemStack());
    }

    @Override
    public void open(@NotNull final Player player, final boolean purge) {
        open(player, 1, null);
    }

    @Override
    public void open(@NotNull final Player player) {
        open(player, 1, null);
    }

    @Override
    public void open(@NotNull final Player player, @NotNull final Consumer<PaginatedGui> consumer) {
        open(player, 1, consumer);
    }

    @Override
    public void open(@NotNull final Player player, final int openPage, @Nullable final Consumer<PaginatedGui> consumer) {
        if (player.isSleeping()) return;

        if (openPage <= getMaxPages() || openPage > 0) this.pageNumber = openPage;

        getInventory().clear();
        this.currentPage.clear();

        populate();

        // calculate anyway, just in case.
        if (this.pageSize == 0 || this.pageSize == getSize()) calculatePageSize();

        populatePage();

        if (consumer != null) {
            consumer.accept(this);
        }

        player.openInventory(getInventory());
    }

    @Override
    public final int getNextPageNumber() {
        if (this.pageNumber + 1 > getMaxPages()) return this.pageNumber;

        return pageNumber + 1;
    }

    @Override
    public final int getPreviousPageNumber() {
        if (this.pageNumber - 1 == 0) return this.pageNumber;

        return this.pageNumber - 1;
    }

    @Override
    public final boolean next() {
        if (this.pageNumber + 1 > getMaxPages()) return false;

        this.pageNumber++;

        updatePage();

        return true;
    }

    @Override
    public final boolean previous() {
        if (this.pageNumber - 1 == 0) return false;

        this.pageNumber--;

        updatePage();

        return true;
    }

    @Override
    public final GuiItem getPageItem(final int slot) {
        return this.currentPage.get(slot);
    }

    @Override
    public void setPageNumber(final int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public final int getCurrentPageNumber() {
        return this.pageNumber;
    }

    @Override
    public final List<GuiItem> getItemsFromPage(final int givenPage) {
        final int page = givenPage - 1;

        final List<GuiItem> guiPage = new ArrayList<>();

        int max = ((page * this.pageSize) + this.pageSize);
        if (max > this.pageItems.size()) max = this.pageItems.size();

        for (int i = page * this.pageSize; i < max; i++) {
            guiPage.add(this.pageItems.get(i));
        }

        return guiPage;
    }

    @Override
    public final Map<Integer, GuiItem> getCurrentPageItems() {
        return this.currentPage;
    }

    @Override
    public void clearPageContents() {
        for (Map.Entry<Integer, GuiItem> entry : this.currentPage.entrySet()) {
            getInventory().setItem(entry.getKey(), null);
        }
    }

    @Override
    public void clearPageItems(boolean update) {
        this.pageItems.clear();

        if (update) {
            updatePage();
        }
    }

    @Override
    public void clearPageItems() {
        clearPageItems(false);
    }

    public final int getMaxPages() {
        if (this.pageSize == 0) this.pageSize = calculatePageSize();

        return (int) Math.ceil((double) this.pageItems.size() / this.pageSize);
    }

    @Override
    public void populatePage() {
        int slot = 0;
        final int inventorySize = getInventory().getSize();

        final Iterator<GuiItem> iterator = getItemsFromPage(this.pageNumber).iterator();

        while (iterator.hasNext()) {
            if (slot >= inventorySize) {
                break; // Exit the loop if slot exceeds inventory size
            }

            if (getGuiItem(slot) != null || getInventory().getItem(slot) != null) {
                slot++;

                continue;
            }

            final GuiItem guiItem = iterator.next();

            this.currentPage.put(slot, guiItem);

            getInventory().setItem(slot, guiItem.getItemStack());

            slot++;
        }
    }

    @Override
    public void updatePage() {
        clearPageContents();

        populatePage();
    }

    @Override
    public final int calculatePageSize() {
        int counter = 0;

        for (int slot = 0; slot < getSize(); slot++) {
            if (getGuiItem(slot) == null) counter++;
        }

        return this.pageSize = Math.max(1, counter);
    }

    @Override
    public void updateInventory(final Player player) {
        getInventory().clear();

        populate();
    }
}