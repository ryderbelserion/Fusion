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

    void setTitle(@NotNull String title);

    @NotNull Component title(@NotNull Audience audience);

    @NotNull Component title();

    int getRows();

    void setRows(int rows);

    int getSize();

    @NotNull GuiType getGuiType();

    @NotNull GuiFiller getFiller();

    void close(@NotNull Player player, @NotNull InventoryCloseEvent.Reason reason, boolean isDelayed);

    void close(@NotNull Player player, boolean isDelayed);

    void close(@NotNull Player player);

    void updateTitle(@NotNull Player player);

    void updateInventory(@NotNull Player player);

    void updateTitles();

    void updateInventories();

    boolean isUpdating();

    void setUpdating(boolean isUpdating);

    void addInteractionComponent(@NotNull InteractionComponent... components);

    void removeInteractionComponent(@NotNull InteractionComponent component);

    boolean canPerformOtherActions();

    boolean isInteractionsDisabled();

    boolean canPlaceItems();

    boolean canTakeItems();

    boolean canSwapItems();

    boolean canDropItems();

    void giveItem(@NotNull Player player, @NotNull ItemStack itemStack);

    void giveItem(@NotNull Player player, @NotNull ItemStack... itemStacks);

    void setItem(int slot, @NotNull GuiItem guiItem);

    void setItem(int row, int col, @NotNull GuiItem guiItem);

    void setItem(@NotNull List<Integer> slots, @NotNull GuiItem guiItem);

    void addItem(@NotNull GuiItem... items);

    void addItem(boolean expandIfFull, @NotNull GuiItem... items);

    void removeItem(int row, int col);

    void removeItem(int slot);

    void setDefaultClickAction(@Nullable GuiAction<@NotNull InventoryClickEvent> defaultClickAction);

    @Nullable GuiItem getGuiItem(int slot);

    void addSlotAction(int slot, @Nullable GuiAction<@NotNull InventoryClickEvent> slotAction);

    @Nullable GuiAction<InventoryClickEvent> getSlotAction(int slot);

    @Nullable GuiAction<InventoryClickEvent> getDefaultTopClickAction();

    void setDefaultTopClickAction(@Nullable GuiAction<@NotNull InventoryClickEvent> defaultTopClickAction);

    @Nullable GuiAction<InventoryClickEvent> getPlayerInventoryAction();

    void setOpenGuiAction(@Nullable GuiAction<@NotNull InventoryOpenEvent> openGuiAction);

    @Nullable GuiAction<InventoryClickEvent> getOutsideClickAction();

    @Nullable GuiAction<InventoryClickEvent> getDefaultClickAction();

    void setDragAction(@Nullable GuiAction<@NotNull InventoryDragEvent> dragAction);

    @Nullable GuiAction<InventoryCloseEvent> getCloseGuiAction();

    void setCloseGuiAction(@Nullable GuiAction<@NotNull InventoryCloseEvent> closeGuiAction);

    @Nullable GuiAction<InventoryOpenEvent> getOpenGuiAction();

    void setPlayerInventoryAction(@Nullable GuiAction<@NotNull InventoryClickEvent> playerInventoryAction);

    @Nullable GuiAction<InventoryDragEvent> getDragAction();

    void setOutsideClickAction(@Nullable GuiAction<@NotNull InventoryClickEvent> outsideClickAction);

    void open(@NotNull Player player, boolean purge);

    void open(@NotNull Player player);

    default @NotNull Set<InteractionComponent> safeCopy(@NotNull Set<InteractionComponent> components) {
        return components.isEmpty() ? EnumSet.noneOf(InteractionComponent.class) : EnumSet.copyOf(components);
    }
}