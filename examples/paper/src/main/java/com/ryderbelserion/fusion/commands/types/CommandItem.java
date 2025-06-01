package com.ryderbelserion.fusion.commands.types;

import com.ryderbelserion.fusion.paper.api.builders.items.ItemBuilder;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import dev.triumphteam.cmd.core.annotations.Syntax;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemType;
import org.bukkit.permissions.PermissionDefault;

public class CommandItem extends BaseCommand {

    @Command("item")
    @Permission(value = "fusion.item", def = PermissionDefault.TRUE)
    @Syntax(value = "/item reload")
    public void reload(Player player) {
        final ItemBuilder itemBuilder = ItemBuilder.from(ItemType.DIAMOND_SWORD).addEnchantment("sharpness", 3).hideComponent("enchantments");

        itemBuilder.addItemToInventory(player.getInventory());
    }
}