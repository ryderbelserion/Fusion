package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.utils.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Fusion extends JavaPlugin implements Listener {

    private FusionPaper fusion;

    @Override
    public void onEnable() {
        this.fusion = new FusionPaper(this, getFile().toPath());
        this.fusion.init();

        getServer().getPluginManager().registerEvents(this, this);
    }

    public @NotNull final FusionPaper getFusion() {
        return this.fusion;
    }

    @EventHandler
    public void onPlayerJoin(@NotNull final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        ItemStack itemStack = ItemUtils.getItemStack("potion[potion_contents={custom_color:15961002,custom_effects:[{id:absorption,duration:400,amplifier:2,ambient:1b},{id:invisibility,duration:200,amplifier:1}]},custom_name=[{\"text\":\"H\",\"italic\":false,\"color\":\"#ff0000\"},{\"text\":\"e\",\"italic\":false,\"color\":\"#cc3938\"},{\"text\":\"l\",\"italic\":false,\"color\":\"#987270\"},{\"text\":\"l\",\"italic\":false,\"color\":\"#65aaa8\"},{\"text\":\"o\",\"italic\":false,\"color\":\"#31e3e0\"}]]");

        if (itemStack == null) {
            return;
        }

        final PlayerInventory inventory = player.getInventory();

        inventory.addItem(itemStack);
    }
}