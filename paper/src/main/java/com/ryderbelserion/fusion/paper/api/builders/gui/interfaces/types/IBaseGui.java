package com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.types;

import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.GuiAction;
import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.GuiFiller;
import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.GuiItem;
import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.GuiType;
import com.ryderbelserion.fusion.paper.api.builders.gui.enums.InteractionComponent;
import net.kyori.adventure.audience.Audience;
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

    @NotNull Map<@NotNull Integer, @NotNull GuiItem> getGuiItems();

    @NotNull String getTitle();

    void setTitle(@NotNull final String title);

    @NotNull Component title(@NotNull final Audience audience);

    @NotNull Component title();

    int getRows();

    void setRows(final int rows);

    int getSize();

    @NotNull GuiType getGuiType();

    @NotNull GuiFiller getFiller();

    void close(@NotNull final Player player, @NotNull final InventoryCloseEvent.Reason reason, final boolean isDelayed);

    void close(@NotNull final Player player, final boolean isDelayed);

    void close(@NotNull final Player player);

    void updateTitle(@NotNull final Player player, @NotNull final Map<String, String> placeholders);

    void updateTitle(@NotNull final Player player);

    void updateInventory(@NotNull final Player player);

    void updateTitles();

    void updateInventories();

    boolean isUpdating();

    void setUpdating(final boolean isUpdating);

    void addInteractionComponent(@NotNull final InteractionComponent... components);

    void removeInteractionComponent(@NotNull final InteractionComponent component);

    boolean canPerformOtherActions();

    boolean isInteractionsDisabled();

    boolean canPlaceItems();

    boolean canTakeItems();

    boolean canSwapItems();

    boolean canDropItems();

    void giveItem(@NotNull final Player player, @NotNull final ItemStack itemStack);

    void giveItem(@NotNull final Player player, @NotNull final ItemStack... itemStacks);

    void setItem(final int slot, @NotNull final GuiItem guiItem);

    void setItem(final int row, final int col, @NotNull final GuiItem guiItem);

    void setItem(@NotNull final List<Integer> slots, @NotNull final GuiItem guiItem);

    void addItem(@NotNull final GuiItem... items);

    void addItem(final boolean expandIfFull, @NotNull final GuiItem... items);

    void removeItem(final int row, final int col);

    void removeItem(final int slot);

    void setDefaultClickAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> defaultClickAction);

    @Nullable GuiItem getGuiItem(final int slot);

    void addSlotAction(final int slot, @Nullable final GuiAction<@NotNull InventoryClickEvent> slotAction);

    @Nullable GuiAction<InventoryClickEvent> getSlotAction(final int slot);

    @Nullable GuiAction<InventoryClickEvent> getDefaultTopClickAction();

    void setDefaultTopClickAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> defaultTopClickAction);

    @Nullable GuiAction<InventoryClickEvent> getPlayerInventoryAction();

    void setOpenGuiAction(@Nullable final GuiAction<@NotNull InventoryOpenEvent> openGuiAction);

    @Nullable GuiAction<InventoryClickEvent> getOutsideClickAction();

    @Nullable GuiAction<InventoryClickEvent> getDefaultClickAction();

    void setDragAction(@Nullable final GuiAction<@NotNull InventoryDragEvent> dragAction);

    @Nullable GuiAction<InventoryCloseEvent> getCloseGuiAction();

    void setCloseGuiAction(@Nullable final GuiAction<@NotNull InventoryCloseEvent> closeGuiAction);

    @Nullable GuiAction<InventoryOpenEvent> getOpenGuiAction();

    void setPlayerInventoryAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> playerInventoryAction);

    @Nullable GuiAction<InventoryDragEvent> getDragAction();

    void setOutsideClickAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> outsideClickAction);

    void open(@NotNull final Player player, final boolean purge);

    void open(@NotNull final Player player);

    default @NotNull Set<InteractionComponent> safeCopy(@NotNull final Set<InteractionComponent> components) {
        return components.isEmpty() ? EnumSet.noneOf(InteractionComponent.class) : EnumSet.copyOf(components);
    }
}