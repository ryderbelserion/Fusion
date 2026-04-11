package com.ryderbelserion.fusion.paper.builders.gui;

import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.builders.folia.FoliaScheduler;
import com.ryderbelserion.fusion.paper.builders.folia.Scheduler;
import com.ryderbelserion.fusion.paper.utils.GuiUtils;
import com.ryderbelserion.fusion.paper.builders.gui.enums.GuiState;
import com.ryderbelserion.fusion.paper.builders.gui.interfaces.GuiAction;
import com.ryderbelserion.fusion.paper.builders.gui.objects.GuiItem;
import com.ryderbelserion.fusion.paper.builders.gui.objects.border.GuiFiller;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public abstract class GuiBuilder<B> implements InventoryHolder, Listener {

    protected final FusionPaper fusion = (FusionPaper) FusionProvider.getInstance();

    protected static final Set<InventoryAction> item_take_actions =
            Collections.unmodifiableSet(EnumSet.of(
                    InventoryAction.PICKUP_ONE,
                    InventoryAction.PICKUP_SOME,
                    InventoryAction.PICKUP_HALF,
                    InventoryAction.PICKUP_ALL,
                    InventoryAction.COLLECT_TO_CURSOR,
                    InventoryAction.HOTBAR_SWAP,
                    InventoryAction.MOVE_TO_OTHER_INVENTORY
            ));

    protected static final Set<InventoryAction> item_drop_actions =
            Collections.unmodifiableSet(EnumSet.of(
                    InventoryAction.DROP_ONE_SLOT,
                    InventoryAction.DROP_ALL_SLOT,
                    InventoryAction.DROP_ONE_CURSOR,
                    InventoryAction.DROP_ALL_CURSOR
            ));

    protected static final Set<InventoryAction> item_place_actions =
            Collections.unmodifiableSet(EnumSet.of(
                    InventoryAction.PLACE_ONE,
                    InventoryAction.PLACE_SOME,
                    InventoryAction.PLACE_ALL
            ));

    protected static final Set<InventoryAction> item_swap_actions =
            Collections.unmodifiableSet(EnumSet.of(
                    InventoryAction.HOTBAR_SWAP,
                    InventoryAction.SWAP_WITH_CURSOR
            ));

    // tracks static items, we use this later for what slots to ignore with pagination.
    protected final Map<Integer, GuiItem> items = new HashMap<>();

    protected final Set<GuiState> states = new HashSet<>();

    protected final Inventory inventory;
    protected final JavaPlugin plugin;
    protected final GuiFiller filler;
    protected final int size;
    protected final int rows;

    protected String title;

    public GuiBuilder(
            @NotNull final JavaPlugin plugin,
            @NotNull final Audience player,
            @NotNull final String title,
            @NotNull final InventoryType type,
            @NotNull final Map<String, String> placeholders
    ) {
        this.plugin = plugin;

        final Server server = this.plugin.getServer();

        this.inventory = server.createInventory(this, type, this.fusion.asComponent(player, this.title = title, placeholders));
        this.filler = new GuiFiller(this);
        this.size = type.getDefaultSize();
        this.rows = this.size / 9;
    }

    public GuiBuilder(
            @NotNull final JavaPlugin plugin,
            @NotNull final String title,
            @NotNull final InventoryType type,
            @NotNull final Map<String, String> placeholders
    ) {
        this(plugin, Audience.empty(), title, type, placeholders);
    }

    public GuiBuilder(
            @NotNull final JavaPlugin plugin,
            @NotNull final Audience player,
            @NotNull final String title,
            final int rows,
            @NotNull final Map<String, String> placeholders
    ) {
        this.plugin = plugin;

        final Server server = this.plugin.getServer();

        this.inventory = server.createInventory(this, this.size = rows * 9, this.fusion.asComponent(player, this.title = title, placeholders));
        this.filler = new GuiFiller(this);
        this.rows = rows;
    }

    public GuiBuilder(
            @NotNull final JavaPlugin plugin,
            @NotNull final String title,
            final int rows,
            @NotNull final Map<String, String> placeholders
    ) {
        this(plugin, Audience.empty(), title, rows, placeholders);
    }

    protected GuiAction<InventoryClickEvent> bottomAction;
    protected GuiAction<InventoryClickEvent> defaultAction;
    protected GuiAction<InventoryClickEvent> topAction;

    protected GuiAction<InventoryCloseEvent> closeAction;
    protected GuiAction<InventoryOpenEvent> openAction;

    public abstract B interact(@NotNull final InventoryClickEvent event);

    public B open(@NotNull final Player player) {
        if (player.isSleeping()) return (B) this;

        player.openInventory(this.inventory);

        return (B) this;
    }

    public B build(final int openPage) {
        return (B) this;
    }

    public B build() {
        return build(1);
    }

    public B removeItem(final int row, final int column) {
        return removeItem(getSlotFromColumn(row, column));
    }

    public B removeItem(final int slot) {
        if (slot > this.size || this.inventory.getItem(slot) == null) {
            return (B) this;
        }

        this.inventory.setItem(slot, null);
        this.items.remove(slot);

        return (B) this;
    }

    public B close(@NotNull final Player player, @NotNull final InventoryCloseEvent.Reason reason, final boolean isDelayed) {
        if (isDelayed) {
            new FoliaScheduler(this.plugin, Scheduler.global_scheduler) {
                @Override
                public void run() {
                    player.closeInventory(reason);
                }
            }.runDelayed(2L);

            return (B) this;
        }

        player.closeInventory(reason);

        return (B) this;
    }

    public B close(@NotNull final Player player, final boolean isDelayed) {
        return close(player, InventoryCloseEvent.Reason.PLUGIN, isDelayed);
    }

    public B close(@NotNull final Player player) {
        return close(player, true);
    }

    public void update(@NotNull final Player player) {
        this.inventory.clear();

        populate();
    }

    @Override
    public @NotNull final Inventory getInventory() {
        return this.inventory;
    }

    public @NotNull final GuiFiller getFiller() {
        return this.filler;
    }

    public @NotNull final String getTitle() {
        return this.title;
    }

    public B setTitle(@NotNull final Player player, @NotNull final String title, @NotNull final Map<String, String> placeholders) {
        GuiUtils.updateTitle(player, this.title = title, placeholders);

        return (B) this;
    }

    public B setTitle(@NotNull final Player player, @NotNull final String title) {
        setTitle(player, title, Map.of());

        return (B) this;
    }

    public B addSlotAction(final int row, final int column, @NotNull final ItemStack itemStack, @NotNull final GuiAction<InventoryClickEvent> action) {
        return addSlotAction(getSlotFromColumn(row, column), itemStack, action);
    }

    public B addSlotAction(final int slot, @NotNull final ItemStack itemStack, @NotNull final GuiAction<InventoryClickEvent> action) {
        this.items.put(slot, new GuiItem(itemStack, action));

        this.inventory.setItem(slot, itemStack);

        return (B) this;
    }

    public B addSlotAction(final int slot, @NotNull final GuiAction<InventoryClickEvent> action) {
        return addSlotAction(slot, ItemType.AIR.createItemStack(), action);
    }

    public B addSlotAction(@NotNull final GuiAction<InventoryClickEvent> action) {
        return addSlotAction(this.inventory.firstEmpty(), action);
    }

    public B addSlotAction(final int slot, @NotNull final ItemStack itemStack) {
        return addSlotAction(slot, itemStack, action -> {});
    }

    public B setDefaultAction(@NotNull final GuiAction<InventoryClickEvent> defaultAction) {
        this.defaultAction = defaultAction;

        return (B) this;
    }

    public B setBottomAction(@NotNull final GuiAction<InventoryClickEvent> bottomAction) {
        this.bottomAction = bottomAction;

        return (B) this;
    }

    public B setCloseAction(@NotNull final GuiAction<InventoryCloseEvent> closeAction) {
        this.closeAction = closeAction;
        
        return (B) this;
    }

    public B setOpenAction(@NotNull final GuiAction<InventoryOpenEvent> openAction) {
        this.openAction = openAction;

        return (B) this;
    }

    public B setTopAction(@NotNull final GuiAction<InventoryClickEvent> topAction) {
        this.topAction = topAction;

        return (B) this;
    }

    public B removeState(@NotNull final GuiState state) {
        this.states.remove(state);

        return (B) this;
    }

    public B addState(@NotNull final GuiState state) {
        this.states.add(state);

        return (B) this;
    }

    public final int getRows() {
        return this.rows;
    }

    // Internal methods
    @ApiStatus.Internal
    protected boolean isTakeItemEvent(@NotNull final InventoryClickEvent event) {
        final Inventory inventory = event.getInventory();
        final Inventory clickedInventory = event.getClickedInventory();
        final InventoryAction action = event.getAction();

        if (clickedInventory != null && clickedInventory.getType() == InventoryType.PLAYER || inventory.getType() == InventoryType.PLAYER) {
            return false;
        }

        return action == InventoryAction.MOVE_TO_OTHER_INVENTORY || isTakeAction(action);
    }

    @ApiStatus.Internal
    protected boolean isPlaceItemEvent(@NotNull final InventoryClickEvent event) {
        final Inventory inventory = event.getInventory();
        final Inventory clickedInventory = event.getClickedInventory();
        final InventoryAction action = event.getAction();

        // shift click on item in player inventory
        if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY && clickedInventory != null && clickedInventory.getType() == InventoryType.PLAYER && inventory.getType() != clickedInventory.getType()) {
            return true;
        }

        // normal click on gui empty slot with item on cursor
        return isPlaceAction(action) && (clickedInventory == null || clickedInventory.getType() != InventoryType.PLAYER) && inventory.getType() != InventoryType.PLAYER;
    }

    @ApiStatus.Internal
    protected boolean isSwapItemEvent(@NotNull final InventoryClickEvent event) {
        final Inventory inventory = event.getInventory();
        final Inventory clickedInventory = event.getClickedInventory();
        final InventoryAction action = event.getAction();

        return isSwapAction(action) && (clickedInventory == null || clickedInventory.getType() != InventoryType.PLAYER) && inventory.getType() != InventoryType.PLAYER;
    }

    @ApiStatus.Internal
    protected boolean isDropItemEvent(@NotNull final InventoryClickEvent event) {
        final Inventory inventory = event.getInventory();
        final Inventory clickedInventory = event.getClickedInventory();
        final InventoryAction action = event.getAction();

        return isDropAction(action) && (clickedInventory != null || inventory.getType() != InventoryType.PLAYER);
    }

    @ApiStatus.Internal
    protected boolean isOtherEvent(@NotNull final InventoryClickEvent event) {
        final Inventory inventory = event.getInventory();
        final Inventory clickedInventory = event.getClickedInventory();
        final InventoryAction action = event.getAction();

        return isOtherAction(action) && (clickedInventory != null || inventory.getType() != InventoryType.PLAYER);
    }

    @ApiStatus.Internal
    protected boolean isPlaceAction(@NotNull final InventoryAction action) {
        return item_place_actions.contains(action);
    }

    @ApiStatus.Internal
    protected boolean isOtherAction(@NotNull final InventoryAction action) {
        return action == InventoryAction.CLONE_STACK || action == InventoryAction.UNKNOWN;
    }

    @ApiStatus.Internal
    protected boolean isTakeAction(@NotNull final InventoryAction action) {
        return item_take_actions.contains(action);
    }

    @ApiStatus.Internal
    protected boolean isSwapAction(@NotNull final InventoryAction action) {
        return item_swap_actions.contains(action);
    }

    @ApiStatus.Internal
    protected boolean isDropAction(@NotNull final InventoryAction action) {
        return item_drop_actions.contains(action);
    }

    @ApiStatus.Internal
    public void close(@NotNull final InventoryCloseEvent event) {
        if (this.closeAction != null) {
            this.closeAction.execute(event);
        }
    }

    @ApiStatus.Internal
    public void open(@NotNull final InventoryOpenEvent event) {
        if (this.openAction != null) {
            this.openAction.execute(event);
        }
    }

    @ApiStatus.Internal
    public void drag(@NotNull final InventoryDragEvent event) {
        if (this.states.contains(GuiState.block_all_interactions)) {
            event.setResult(Event.Result.DENY);

            return;
        }

        if (!this.states.contains(GuiState.block_item_place)) {
            return;
        }

        event.setResult(Event.Result.DENY);
    }

    protected int getSlotFromColumn(final int row, final int column) {
        return (column + (row - 1) * 9) - 1;
    }

    protected void populate() {
        for (final Map.Entry<Integer, GuiItem> entry : this.items.entrySet()) {
            this.inventory.setItem(entry.getKey(), entry.getValue().getItemStack());
        }
    }
}