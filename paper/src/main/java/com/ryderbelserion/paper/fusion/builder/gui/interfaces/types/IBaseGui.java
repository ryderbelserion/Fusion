package com.ryderbelserion.paper.fusion.builder.gui.interfaces.types;

import com.ryderbelserion.paper.fusion.builder.gui.interfaces.GuiAction;
import com.ryderbelserion.paper.fusion.builder.gui.interfaces.GuiFiller;
import com.ryderbelserion.paper.fusion.builder.gui.interfaces.GuiItem;
import com.ryderbelserion.paper.fusion.builder.gui.interfaces.GuiType;
import com.ryderbelserion.paper.fusion.builder.gui.enums.InteractionComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IBaseGui {

    Map<@NotNull Integer, @NotNull GuiItem> getGuiItems();

    String getTitle();

    void setTitle(final String title);

    Component title();

    int getRows();

    void setRows(final int rows);

    int getSize();

    GuiType getGuiType();

    GuiFiller getFiller();

    void close(final Player player, final InventoryCloseEvent.Reason reason, final boolean isDelayed);

    void close(final Player player, final boolean isDelayed);

    void close(final Player player);

    void updateTitle(final Player player);

    void updateInventory(final Player player);

    void updateTitles();

    void updateInventories();

    boolean isUpdating();

    void setUpdating(final boolean isUpdating);

    void addInteractionComponent(final InteractionComponent... components);

    void removeInteractionComponent(final InteractionComponent component);

    boolean canPerformOtherActions();

    boolean isInteractionsDisabled();

    boolean canPlaceItems();

    boolean canTakeItems();

    boolean canSwapItems();

    boolean canDropItems();

    void giveItem(final Player player, final ItemStack itemStack);

    void giveItem(final Player player, final ItemStack... itemStacks);

    void setItem(final int slot, final GuiItem guiItem);

    void setItem(final int row, final int col, @NotNull final GuiItem guiItem);

    void setItem(@NotNull final List<Integer> slots, @NotNull final GuiItem guiItem);

    void addItem(@NotNull final GuiItem... items);

    void addItem(final boolean expandIfFull, @NotNull final GuiItem... items);

    void removeItem(final int row, final int col);

    void removeItem(final int slot);

    void setDefaultClickAction(final @Nullable GuiAction<@NotNull InventoryClickEvent> defaultClickAction);

    @Nullable GuiItem getGuiItem(final int slot);

    void addSlotAction(final int slot, @Nullable final GuiAction<@NotNull InventoryClickEvent> slotAction);

    @Nullable GuiAction<InventoryClickEvent> getSlotAction(final int slot);

    GuiAction<InventoryClickEvent> getDefaultTopClickAction();

    void setDefaultTopClickAction(final @Nullable GuiAction<@NotNull InventoryClickEvent> defaultTopClickAction);

    GuiAction<InventoryClickEvent> getPlayerInventoryAction();

    void setOpenGuiAction(final @Nullable GuiAction<@NotNull InventoryOpenEvent> openGuiAction);

    GuiAction<InventoryClickEvent> getOutsideClickAction();

    GuiAction<InventoryClickEvent> getDefaultClickAction();

    void setDragAction(final @Nullable GuiAction<@NotNull InventoryDragEvent> dragAction);

    GuiAction<InventoryCloseEvent> getCloseGuiAction();

    void setCloseGuiAction(final @Nullable GuiAction<@NotNull InventoryCloseEvent> closeGuiAction);

    GuiAction<InventoryOpenEvent> getOpenGuiAction();

    void setPlayerInventoryAction(final @Nullable GuiAction<@NotNull InventoryClickEvent> playerInventoryAction);

    GuiAction<InventoryDragEvent> getDragAction();

    void setOutsideClickAction(final @Nullable GuiAction<@NotNull InventoryClickEvent> outsideClickAction);

    void open(final Player player, final boolean purge);

    void open(final Player player);

    default Set<InteractionComponent> safeCopy(final Set<InteractionComponent> components) {
        return components.isEmpty() ? EnumSet.noneOf(InteractionComponent.class) : EnumSet.copyOf(components);
    }
}