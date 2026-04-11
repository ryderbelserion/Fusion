package com.ryderbelserion.fusion.paper.builders.gui.types.paginated;

import com.ryderbelserion.fusion.paper.builders.gui.GuiBuilder;
import com.ryderbelserion.fusion.paper.builders.gui.enums.GuiState;
import com.ryderbelserion.fusion.paper.builders.gui.interfaces.GuiAction;
import com.ryderbelserion.fusion.paper.builders.gui.objects.GuiItem;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class PaginatedGui extends GuiBuilder<PaginatedGui> {

    protected final Map<Integer, GuiItem> pageItems = new HashMap<>(); // items to loop, and populate through later.

    protected final Map<Integer, GuiItem> currentPage;

    private int pageNumber = 1;
    private int pageSize;

    public PaginatedGui(
            @NotNull final JavaPlugin plugin,
            @NotNull final Audience player,
            @NotNull final String title,
            @NotNull final InventoryType type
    ) {
        this(plugin, player, title, 0, type.getDefaultSize() / 9);
    }

    public PaginatedGui(
            @NotNull final JavaPlugin plugin,
            @NotNull final Audience player,
            @NotNull final String title,
            final int pageSize,
            @NotNull final InventoryType type
    ) {
        this(plugin, player, title, pageSize, type.getDefaultSize() / 9);
    }

    public PaginatedGui(
            @NotNull final JavaPlugin plugin,
            @NotNull final Audience player,
            @NotNull final String title,
            final int rows
    ) {
        this(plugin, player, title, 0, rows);
    }

    public PaginatedGui(
            @NotNull final JavaPlugin plugin,
            @NotNull final Audience player,
            @NotNull final String title,
            final int pageSize, final int rows
    ) {
        super(plugin, player, title, rows);

        this.pageSize = pageSize == 0 ? calculate() : pageSize;
        this.currentPage = new HashMap<>(this.size);
    }

    @Override
    public PaginatedGui build(final int openPage) {
        if (openPage <= getMaxPages() || openPage > 0) this.pageNumber = openPage;

        this.inventory.clear();

        this.currentPage.clear();

        populate();

        if (this.pageSize == 0 || this.pageSize == this.size) calculate();

        populatePage();

        return this;
    }

    @Override
    public PaginatedGui interact(@NotNull final InventoryClickEvent event) {
        final int slot = event.getSlot();

        if (this.states.contains(GuiState.block_all_interactions)) {
            event.setResult(Event.Result.DENY);
        } else {
            if ((this.states.contains(GuiState.block_item_place) && isPlaceItemEvent(event)) ||
                    (this.states.contains(GuiState.block_item_take) && isTakeItemEvent(event)) ||
                    (this.states.contains(GuiState.block_item_swap)  && isSwapItemEvent(event)) ||
                    (this.states.contains(GuiState.block_item_drop) && isDropItemEvent(event)) ||
                    (this.states.contains(GuiState.block_other_actions) && isOtherEvent(event))
            ) {
                event.setResult(Event.Result.DENY);
            }
        }

        final Inventory inventory = event.getClickedInventory();

        if (inventory == null) return this;

        final InventoryType type = inventory.getType();

        if (this.topAction != null && type != InventoryType.PLAYER) {
            this.topAction.execute(event);
        }

        if (this.bottomAction != null && type == InventoryType.PLAYER) {
            this.bottomAction.execute(event);
        }

        if (this.defaultAction != null) {
            this.defaultAction.execute(event);
        }

        GuiItem guiItem = this.items.get(slot);

        if (guiItem == null) {
            guiItem = this.pageItems.get(slot);
        }

        if (guiItem == null) {
            return this;
        }

        guiItem.getAction().execute(event);

        return this;
    }

    public PaginatedGui setPageItem(
            final int slot,
            @NotNull final ItemStack itemStack,
            @NotNull final GuiAction<InventoryClickEvent> action
    ) {
        return setPageItem(slot, new GuiItem(itemStack, action));
    }

    public PaginatedGui setPageItem(final int slot, @NotNull final GuiAction<InventoryClickEvent> action) {
        return setPageItem(slot, ItemType.AIR.createItemStack(), action);
    }

    public PaginatedGui setPageItem(@NotNull final GuiAction<InventoryClickEvent> action) {
        return setPageItem(this.inventory.firstEmpty(), action);
    }

    public PaginatedGui setPageItem(final int slot, @NotNull final ItemStack itemStack) {
        if (!this.currentPage.containsKey(slot)) return this;

        if (this.items.containsKey(slot)) return this; // if filler item exists, do not add.

        final GuiItem guiItem = this.currentPage.get(slot);

        this.inventory.setItem(slot, guiItem.setItemStack(itemStack));

        return this;
    }

    public PaginatedGui setPageItem(final int slot, @NotNull final GuiItem guiItem) {
        if (this.items.containsKey(slot)) return this; // if filler item exists, do not add.

        final boolean hasItem = this.currentPage.containsKey(slot);

        this.currentPage.put(slot, guiItem);
        this.pageItems.put(slot, guiItem);

        if (!hasItem) {
            updatePage();
        } else {
            this.inventory.setItem(slot, guiItem.getItemStack());
        }

        return this;
    }

    public void setPageItem(final int row, final int column, @NotNull final ItemStack itemStack, @NotNull final GuiAction<InventoryClickEvent> action) {
        setPageItem(row, column, new GuiItem(itemStack, action));
    }

    public void setPageItem(final int row, final int column, @NotNull final ItemStack itemStack) {
        setPageItem(getSlotFromColumn(row, column), itemStack);
    }

    public void setPageItem(final int row, final int column, @NotNull final GuiItem guiItem) {
        setPageItem(getSlotFromColumn(row, column), guiItem);
    }

    public void addPageItem(@NotNull final GuiItem guiItem) {
        final int slot = guiItem.getSlot();

        if (this.items.containsKey(slot)) return; // if filler item exists, do not add.

        this.pageItems.put(slot, guiItem);

        updatePage();
    }

    public void addPageItem(@NotNull final GuiItem... items) {
        for (final GuiItem guiItem : items) {
            addPageItem(guiItem);
        }
    }

    public void removePageItem(final int slot) {
        if (!this.currentPage.containsKey(slot)) return;

        this.currentPage.remove(slot);
        this.pageItems.remove(slot);

        updatePage();
    }

    public final int getPreviousPage() {
        if (this.pageNumber - 1 == 0) return this.pageNumber;

        return this.pageNumber - 1;
    }

    public final int getNextPage() {
        if (this.pageNumber + 1 > getMaxPages()) return this.pageNumber;

        return this.pageNumber + 1;
    }

    public final boolean previousPage() {
        if (this.pageNumber - 1 == 0) return false;

        this.pageNumber--;

        updatePage();

        return true;
    }

    public final boolean nextPage() {
        if (this.pageNumber + 1 > getMaxPages()) return false;

        this.pageNumber++;

        updatePage();

        return true;
    }

    public final int getMaxPages() {
        if (this.pageSize == 0) {
            this.pageSize = calculate();
        }

        return (int) Math.ceil((double) this.pageItems.size() / this.pageSize);
    }

    public final int getPageNumber() {
        return this.pageNumber;
    }

    public void setPageNumber(final int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    public final int getPageSize() {
        return this.pageSize;
    }

    public final int calculate() {
        int counter = 0;

        for (int slot = 0; slot < this.size; slot++) {
            // do not count static slots for pagination.
            if (!this.items.containsKey(slot) && this.inventory.getItem(slot) == null) {
                counter++;
            }
        }

        return this.pageSize = Math.max(1, counter);
    }

    public List<GuiItem> getPageItems(final int page) {
        final int target = page - 1;

        final List<GuiItem> guiPage = new ArrayList<>();

        int max = ((target * this.pageSize) + this.pageSize);
        if (max > this.pageItems.size()) max = this.pageItems.size();

        for (int slot = target * this.pageSize; slot < max; slot++) {
            guiPage.add(this.pageItems.get(slot));
        }

        return guiPage;
    }

    public void populatePage() {
        final Iterator<GuiItem> iterator = getPageItems(this.pageNumber).iterator();

        int slot = 0;

        while (iterator.hasNext()) {
            if (slot >= this.size) {
                break; // Exit the loop if slot exceeds inventory size
            }

            // do not count static slots for pagination.
            if (this.items.containsKey(slot) || this.inventory.getItem(slot) != null) {
                slot++;

                continue;
            }

            final GuiItem guiItem = iterator.next();

            this.inventory.setItem(slot, guiItem.getItemStack());
            this.currentPage.put(slot, guiItem);

            slot++;
        }
    }

    public void updatePage() {
        this.currentPage.keySet().forEach(slot -> this.inventory.setItem(slot, null));

        populatePage();
    }

    public static @NotNull PaginatedGui gui(
            @NotNull final JavaPlugin plugin,
            @NotNull final Audience player,
            @NotNull final String title,
            final int pageSize,
            @NotNull final InventoryType type
    ) {
        return new PaginatedGui(plugin, player, title, pageSize, type);
    }

    public static @NotNull PaginatedGui gui(
            @NotNull final JavaPlugin plugin,
            @NotNull final Audience player,
            @NotNull final String title,
            @NotNull final InventoryType type
    ) {
        return new PaginatedGui(plugin, player, title, 0, type);
    }

    public static @NotNull PaginatedGui gui(
            @NotNull final JavaPlugin plugin,
            @NotNull final String title,
            @NotNull final InventoryType type
    ) {
        return new PaginatedGui(plugin, Audience.empty(), title, type);
    }

    public static @NotNull PaginatedGui gui(
            @NotNull final JavaPlugin plugin,
            @NotNull final Audience player,
            @NotNull final String title,
            final int pageSize, final int rows
    ) {
        return new PaginatedGui(plugin, player, title, pageSize, rows);
    }

    public static @NotNull PaginatedGui gui(
            @NotNull final JavaPlugin plugin,
            @NotNull final Audience player,
            @NotNull final String title, final int rows
    ) {
        return new PaginatedGui(plugin, player, title, 0, rows);
    }

    public static @NotNull PaginatedGui gui(
            @NotNull final JavaPlugin plugin,
            @NotNull final String title, final int rows
    ) {
        return new PaginatedGui(plugin, Audience.empty(), title, rows);
    }
}