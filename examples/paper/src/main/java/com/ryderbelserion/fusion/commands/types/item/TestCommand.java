package com.ryderbelserion.fusion.commands.types.item;

import com.ryderbelserion.fusion.commands.types.BaseCommand;
import com.ryderbelserion.fusion.paper.builder.items.modern.ItemBuilder;
import com.ryderbelserion.fusion.paper.builder.items.modern.types.SkullBuilder;
import dev.triumphteam.cmd.core.annotations.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemType;

public class TestCommand extends BaseCommand {

    @Command("test")
    public void test(final Player player) {
        /*final FireworkBuilder builder = ItemBuilder.from(ItemType.FIREWORK_ROCKET).asFireworkBuilder();

        builder.addEffect(true, false, FireworkEffect.Type.BURST, List.of(
                Color.WHITE,
                Color.RED
        ), null).withDuration(5);

        builder.addItemToInventory(player.getInventory());*/

        final SkullBuilder builder = ItemBuilder.from(ItemType.PLAYER_HEAD).asSkullBuilder();

        builder.withUrl("1ee3126ff2c343da525eef2b93272b9fed36273d0ea08c2616b80009948ad57e");

        builder.addItemToInventory(player.getInventory());
    }
}