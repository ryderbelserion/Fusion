package com.ryderbelserion.fusion.paper.builders.gui.types;

import com.ryderbelserion.fusion.paper.builders.gui.GuiBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        final Inventory inventory = event.getInventory();

        if (inventory.getHolder(false) instanceof GuiBuilder gui) {
            gui.interact(event);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        final Inventory inventory = event.getInventory();

        if (inventory.getHolder(false) instanceof GuiBuilder gui) {
            gui.drag(event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        final Inventory inventory = event.getInventory();

        if (inventory.getHolder(false) instanceof GuiBuilder gui) {
            gui.close(event);
        }
    }

    @EventHandler
    public void onGuiOpen(final InventoryOpenEvent event) {
        final Inventory inventory = event.getInventory();

        if (inventory.getHolder(false) instanceof GuiBuilder gui) {
            gui.open(event);
        }
    }
}