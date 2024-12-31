package com.ryderbelserion.fusion.commands.types.item;

import com.ryderbelserion.fusion.commands.types.BaseCommand;
import com.ryderbelserion.paper.builder.items.modern.ItemBuilder;
import com.ryderbelserion.paper.builder.items.modern.types.fireworks.FireworkBuilder;
import dev.triumphteam.cmd.core.annotations.Command;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemType;
import java.util.List;

public class TestCommand extends BaseCommand {

    @Command("test")
    public void test(final Player player) {
        final FireworkBuilder builder = ItemBuilder.from(ItemType.FIREWORK_ROCKET).asFireworkBuilder();

        builder.addEffect(true, false, FireworkEffect.Type.BURST, List.of(
                Color.WHITE,
                Color.RED
        ), null).withDuration(5);

        builder.addItemToInventory(player.getInventory());
    }
}