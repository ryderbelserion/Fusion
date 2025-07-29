package com.ryderbelserion.listeners;

import com.ryderbelserion.fusion.paper.builders.ItemBuilder;
import com.ryderbelserion.fusion.paper.builders.types.SpawnerBuilder;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.PlayerInventory;

public class ItemListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final PlayerInventory inventory = player.getInventory();

        final ItemBuilder builder = new ItemBuilder(ItemType.SPAWNER, 1, item -> {
            item.withDisplayName("<red>" + PlainTextComponentSerializer.plainText().serialize(
                    player.displayName()
            ) + "</red>");
        });

        final SpawnerBuilder spawner = builder.asSpawnerBuilder();

        spawner.withEntityType(EntityType.CREEPER).build();

        builder.addItemToInventory(player, inventory);
    }
}