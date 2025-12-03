package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.addons.v2.ExtensionManager;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.builders.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Fusion extends JavaPlugin implements Listener {

    private final FusionPaper fusion;

    public Fusion(@NotNull final FusionPaper fusion) {
        this.fusion = fusion;
    }

    @Override
    public void onEnable() {
        final ExtensionManager manager = new ExtensionManager(getDataPath().resolve("extensions"));

        manager.init(1);

        this.fusion.setPlugin(this).init();

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        final Inventory inventory = player.getInventory();

        inventory.addItem(ItemBuilder.from(ItemType.POTION)
                .setColor("229,164,229")
                .asItemStack());

        inventory.addItem(ItemBuilder.from(ItemType.POTION)
                .setColor("yellow")
                .asItemStack());
    }
}