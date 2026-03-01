package com.ryderbelserion.fusion.paper.builders.gui.types.simple;

import com.ryderbelserion.fusion.paper.builders.gui.GuiBuilder;
import com.ryderbelserion.fusion.paper.builders.gui.enums.GuiState;
import com.ryderbelserion.fusion.paper.builders.gui.objects.GuiItem;
import net.kyori.adventure.audience.Audience;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class SimpleGui extends GuiBuilder<SimpleGui> {

    public SimpleGui(@NotNull final JavaPlugin plugin, @NotNull final Audience player, @NotNull final String title, final int rows) {
        super(plugin, player, title, rows);
    }

    public SimpleGui interact(@NotNull final InventoryClickEvent event) {
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

        if (this.items.containsKey(slot) && type != InventoryType.PLAYER) {
            final GuiItem item = this.items.get(slot);

            item.getAction().execute(event);
        }

        return this;
    }

    public static @NotNull SimpleGui gui(@NotNull final JavaPlugin plugin, @NotNull final Audience player, @NotNull final String title, final int rows) {
        return new SimpleGui(plugin, player, title, rows);
    }

    public static @NotNull SimpleGui gui(@NotNull final JavaPlugin plugin, @NotNull final String title, final int rows) {
        return gui(plugin, Audience.empty(), title, rows);
    }
}