package com.ryderbelserion.paper.builder.gui.v2.types;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;

public class SimpleMenu {

    private final InventoryView inventoryView;

    public SimpleMenu(final Player player, final MenuType menuType, final Component title) {
        this.inventoryView = menuType.create(player, title);
    }

    public InventoryView getInventoryView() {
        return this.inventoryView;
    }

    public Component title() {
        return getInventoryView().title();
    }

    public String getTitle() {
        return PlainTextComponentSerializer.plainText().serialize(title());
    }
}