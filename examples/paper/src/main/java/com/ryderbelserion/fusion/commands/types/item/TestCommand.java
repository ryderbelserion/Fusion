package com.ryderbelserion.fusion.commands.types.item;

import com.ryderbelserion.fusion.commands.types.BaseCommand;
import com.ryderbelserion.fusion.paper.api.builder.items.modern.ItemBuilder;
import dev.triumphteam.cmd.core.annotations.Command;
import org.bukkit.entity.Player;

public class TestCommand extends BaseCommand {

    @Command("test")
    public void test(final Player player) {
        /*final FireworkBuilder builder = ItemBuilder.from(ItemType.FIREWORK_ROCKET).asFireworkBuilder();

        builder.addEffect(true, false, FireworkEffect.Type.BURST, List.of(
                Color.WHITE,
                Color.RED
        ), null).withDuration(5);

        builder.addItemToInventory(player.getInventory());*/

        final ItemBuilder builder = ItemBuilder.from("minecraft:diamond_axe");

        builder.addItemToInventory(player.getInventory());
    }
}