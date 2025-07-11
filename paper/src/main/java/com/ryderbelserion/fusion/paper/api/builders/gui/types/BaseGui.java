package com.ryderbelserion.fusion.paper.api.builders.gui.types;

import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.api.FusionCore;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.GuiAction;
import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.GuiFiller;
import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.GuiItem;
import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.GuiType;
import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.types.IBaseGui;
import com.ryderbelserion.fusion.paper.api.builders.gui.enums.InteractionComponent;
import com.ryderbelserion.fusion.paper.api.scheduler.FoliaScheduler;
import com.ryderbelserion.fusion.paper.api.enums.Scheduler;
import com.ryderbelserion.fusion.paper.utils.ColorUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BaseGui implements InventoryHolder, Listener, IBaseGui {

    private final FusionCore fusion = FusionProvider.get();

    private final JavaPlugin plugin;

    private final Server server;

    private final GuiFiller filler = new GuiFiller(this);

    private final Map<Integer, GuiAction<InventoryClickEvent>> slotActions;
    private final Map<Integer, GuiItem> guiItems;

    private final Set<InteractionComponent> interactionComponents;

    private GuiAction<InventoryClickEvent> defaultTopClickAction;
    private GuiAction<InventoryClickEvent> playerInventoryAction;
    private GuiAction<InventoryClickEvent> outsideClickAction;
    private GuiAction<InventoryClickEvent> defaultClickAction;
    private GuiAction<InventoryCloseEvent> closeGuiAction;
    private GuiAction<InventoryOpenEvent> openGuiAction;
    private GuiAction<InventoryDragEvent> dragAction;

    private GuiType guiType = GuiType.CHEST;

    private final Inventory inventory;
    private boolean isUpdating;
    private String title;
    private int rows;

    public BaseGui(@NotNull final JavaPlugin plugin, @NotNull final Audience audience, @NotNull final String title, final int rows, @NotNull final Set<InteractionComponent> components) {
        this.plugin = plugin;
        this.server = this.plugin.getServer();
        this.title = title;
        this.rows = rows;

        int size = this.rows * 9;

        this.slotActions = new LinkedHashMap<>(size);
        this.guiItems = new LinkedHashMap<>(size);

        this.interactionComponents = safeCopy(components);

        this.inventory = this.server.createInventory(this, size, title(audience));
    }

    public BaseGui(@NotNull final JavaPlugin plugin, @NotNull final String title, final int rows, @NotNull final Set<InteractionComponent> components) {
        this(plugin, Audience.empty(), title, rows, components);
    }

    public BaseGui(@NotNull final JavaPlugin plugin, @NotNull final Audience audience, @NotNull final String title, @NotNull final GuiType guiType, @NotNull final Set<InteractionComponent> components) {
        this.plugin = plugin;
        this.server = this.plugin.getServer();
        this.title = title;

        this.slotActions = new LinkedHashMap<>(guiType.getLimit());
        this.guiItems = new LinkedHashMap<>(guiType.getLimit());

        this.interactionComponents = safeCopy(components);

        this.inventory = this.server.createInventory(this, guiType.getInventoryType(), title(audience));

        this.guiType = guiType;
    }

    public BaseGui(@NotNull final JavaPlugin plugin, @NotNull final String title, @NotNull final GuiType guiType, @NotNull final Set<InteractionComponent> components) {
        this(plugin, Audience.empty(), title, guiType, components);
    }

    @Override
    public @NotNull final Map<Integer, GuiItem> getGuiItems() {
        return Collections.unmodifiableMap(this.guiItems);
    }

    @Override
    public @NotNull final String getTitle() {
        return PlainTextComponentSerializer.plainText().serialize(title());
    }

    @Override
    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    @Override
    public @NotNull final Component title(@NotNull final Audience audience) {
        return this.fusion.color(audience, this.title, new HashMap<>());
    }

    @Override
    public @NotNull final Component title() {
        return title(Audience.empty());
    }

    @Override
    public final int getRows() {
        return this.rows;
    }

    @Override
    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public final int getSize() {
        return getRows() * 9;
    }

    @Override
    public @NotNull final GuiType getGuiType() {
        return this.guiType;
    }

    @Override
    public @NotNull final GuiFiller getFiller() {
        return this.filler;
    }

    @Override
    public void addInteractionComponent(@NotNull final InteractionComponent... components) {
        this.interactionComponents.addAll(Arrays.asList(components));
    }

    @Override
    public void removeInteractionComponent(@NotNull final InteractionComponent component) {
        this.interactionComponents.remove(component);
    }

    @Override
    public final boolean canPerformOtherActions() {
        return !this.interactionComponents.contains(InteractionComponent.PREVENT_OTHER_ACTIONS);
    }

    @Override
    public final boolean isInteractionsDisabled() {
        return this.interactionComponents.size() == InteractionComponent.VALUES.size();
    }

    @Override
    public final boolean canPlaceItems() {
        return !this.interactionComponents.contains(InteractionComponent.PREVENT_ITEM_PLACE);
    }

    @Override
    public final boolean canTakeItems() {
        return !this.interactionComponents.contains(InteractionComponent.PREVENT_ITEM_TAKE);
    }

    @Override
    public final boolean canSwapItems() {
        return !this.interactionComponents.contains(InteractionComponent.PREVENT_ITEM_SWAP);
    }

    @Override
    public final boolean canDropItems() {
        return !this.interactionComponents.contains(InteractionComponent.PREVENT_ITEM_DROP);
    }

    @Override
    public void giveItem(@NotNull final Player player, @NotNull final ItemStack itemStack) {
        player.getInventory().addItem(itemStack);
    }

    @Override
    public void giveItem(@NotNull final Player player, @NotNull final ItemStack... itemStacks) {
        Arrays.asList(itemStacks).forEach(item -> giveItem(player, item));
    }

    @Override
    public void setItem(final int slot, @NotNull final GuiItem guiItem) {
        validateSlot(slot);

        this.guiItems.put(slot, guiItem);
        this.inventory.setItem(slot, guiItem.getItemStack());
    }

    @Override
    public void setItem(final int row, final int col, @NotNull final GuiItem guiItem) {
        setItem(getSlotFromRowColumn(row, col), guiItem);
    }

    @Override
    public void setItem(@NotNull final List<Integer> slots, @NotNull final GuiItem guiItem) {
        for (int slot : slots) {
            setItem(slot, guiItem);
        }
    }

    @Override
    public void addItem(@NotNull final GuiItem... items) {
        addItem(false, items);
    }

    @Override
    public void addItem(final boolean expandIfFull, @NotNull final GuiItem... items) {
        final List<GuiItem> notAddedItems = new ArrayList<>();

        for (GuiItem guiItem : items) {
            for (int slot = 0; slot < this.rows * 9; slot++) {
                if (this.guiItems.get(slot) != null) {

                    if (slot == this.rows * 9 - 1) {
                        notAddedItems.add(guiItem);
                    }

                    continue;
                }

                this.guiItems.put(slot, guiItem);

                break;
            }
        }

        if (!expandIfFull || this.rows >= 6 || notAddedItems.isEmpty() || (this.guiType != null && this.guiType != GuiType.CHEST)) {
            return;
        }

        this.rows++;
        this.updateInventories();

        this.addItem(true, notAddedItems.toArray(new GuiItem[0]));
    }

    @Override
    public void removeItem(final int row, final int col) {
        removeItem(getSlotFromRowColumn(row, col));
    }

    @Override
    public void removeItem(final int slot) {
        validateSlot(slot);

        this.guiItems.remove(slot);
    }

    @Override
    public @Nullable final GuiItem getGuiItem(final int slot) {
        return this.guiItems.get(slot);
    }

    public void addSlotAction(final int slot, @Nullable final GuiAction<@NotNull InventoryClickEvent> slotAction) {
        this.slotActions.put(slot, slotAction);
    }

    @Override
    public @Nullable final GuiAction<InventoryClickEvent> getSlotAction(final int slot) {
        return this.slotActions.get(slot);
    }

    @Override
    public void close(@NotNull final Player player, @NotNull final InventoryCloseEvent.Reason reason, final boolean isDelayed) {
        if (isDelayed) {
            new FoliaScheduler(this.plugin, Scheduler.global_scheduler) {
                @Override
                public void run() {
                    player.closeInventory(reason);
                }
            }.runDelayed(2L);

            return;
        }

        player.closeInventory(reason);
    }

    @Override
    public void close(@NotNull final Player player, final boolean isDelayed) {
        close(player, InventoryCloseEvent.Reason.PLUGIN, isDelayed);
    }

    @Override
    public void close(@NotNull final Player player) {
        close(player, true);
    }

    @Override
    public void updateTitle(@NotNull final Player player, @NotNull final Map<String, String> placeholders) {
        ColorUtils.updateTitle(player, this.title, placeholders);
    }

    @Override
    public void updateTitle(@NotNull final Player player) {
        updateTitle(player, new HashMap<>());
    }

    @Override
    public void updateInventory(@NotNull final Player player) {
        this.inventory.clear();

        populate();

        player.updateInventory();
    }

    @Override
    public void updateTitles() {
        this.server.getOnlinePlayers().forEach(player -> {
            final InventoryHolder inventory = player.getOpenInventory().getTopInventory().getHolder(false);

            if (!(inventory instanceof BaseGui)) return;

            updateTitle(player);
        });
    }

    @Override
    public void updateInventories() {
        final Inventory inventory = this.inventory;

        inventory.getViewers().forEach(humanEntity -> {
            if (humanEntity instanceof Player player) {
                updateInventory(player);
            }
        });
    }

    @Override
    public final boolean isUpdating() {
        return this.isUpdating;
    }

    @Override
    public void setUpdating(final boolean isUpdating) {
        this.isUpdating = isUpdating;
    }

    @Override
    public @Nullable final GuiAction<InventoryClickEvent> getDefaultClickAction() {
        return this.defaultClickAction;
    }

    @Override
    public void setDefaultClickAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> defaultClickAction) {
        this.defaultClickAction = defaultClickAction;
    }

    @Override
    public @Nullable final GuiAction<InventoryClickEvent> getDefaultTopClickAction() {
        return this.defaultTopClickAction;
    }

    @Override
    public void setDefaultTopClickAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> defaultTopClickAction) {
        this.defaultTopClickAction = defaultTopClickAction;
    }

    @Override
    public @Nullable final GuiAction<InventoryClickEvent> getPlayerInventoryAction() {
        return this.playerInventoryAction;
    }

    @Override
    public void setPlayerInventoryAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> playerInventoryAction) {
        this.playerInventoryAction = playerInventoryAction;
    }

    @Override
    public @Nullable final GuiAction<InventoryDragEvent> getDragAction() {
        return this.dragAction;
    }

    @Override
    public void setDragAction(@Nullable final GuiAction<@NotNull InventoryDragEvent> dragAction) {
        this.dragAction = dragAction;
    }

    @Override
    public @Nullable final GuiAction<InventoryCloseEvent> getCloseGuiAction() {
        return this.closeGuiAction;
    }

    @Override
    public void setCloseGuiAction(@Nullable final GuiAction<@NotNull InventoryCloseEvent> closeGuiAction) {
        this.closeGuiAction = closeGuiAction;
    }

    @Override
    public @Nullable final GuiAction<InventoryOpenEvent> getOpenGuiAction() {
        return this.openGuiAction;
    }

    @Override
    public void setOpenGuiAction(@Nullable final GuiAction<@NotNull InventoryOpenEvent> openGuiAction) {
        this.openGuiAction = openGuiAction;
    }

    @Override
    public @Nullable final GuiAction<InventoryClickEvent> getOutsideClickAction() {
        return this.outsideClickAction;
    }

    @Override
    public void setOutsideClickAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> outsideClickAction) {
        this.outsideClickAction = outsideClickAction;
    }

    @Override
    public void open(@NotNull final Player player, final boolean purge) {
        if (player.isSleeping()) return;

        if (purge) {
            this.inventory.clear();

            populate();
        }

        player.openInventory(this.inventory);
    }

    @Override
    public void open(@NotNull final Player player) {
        open(player, true);
    }

    @Override
    public @NotNull final Inventory getInventory() {
        return this.inventory;
    }

    public @NotNull final GuiItem asGuiItem(@NotNull final ItemStack itemStack) {
        return asGuiItem(itemStack, null);
    }

    public @NotNull final GuiItem asGuiItem(@NotNull final ItemStack itemStack, @Nullable final GuiAction<InventoryClickEvent> action) {
        return new GuiItem(itemStack, action);
    }

    public void populate() {
        for (final Map.Entry<Integer, GuiItem> entry : this.guiItems.entrySet()) {
            this.inventory.setItem(entry.getKey(), entry.getValue().getItemStack());
        }
    }

    public final int getSlotFromRowColumn(final int row, final int col) {
        return (col + (row - 1) * 9) - 1;
    }

    private void validateSlot(final int slot) {
        final int limit = this.guiType.getLimit();

        if (this.guiType == GuiType.CHEST) {
            if (slot < 0 || slot >= this.rows * limit) throwInvalidSlot(slot);

            return;
        }

        if (slot < 0 || slot > limit) throwInvalidSlot(slot);
    }

    private void throwInvalidSlot(final int slot) {
        if (this.guiType == GuiType.CHEST) {
            throw new FusionException(String.format("Slot %s is not valid for the gui type %s and rows %s!", slot, this.guiType.name(), this.rows));
        }

        throw new FusionException(String.format("Slot %s is not valid for the gui type %s!", slot, this.guiType.name()));
    }
}