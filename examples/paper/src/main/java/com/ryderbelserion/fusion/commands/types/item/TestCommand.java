package com.ryderbelserion.fusion.commands.types.item;

import com.ryderbelserion.fusion.commands.types.BaseCommand;
import com.ryderbelserion.fusion.paper.api.builder.items.modern.ItemBuilder;
import dev.triumphteam.cmd.core.annotations.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemType;
import java.util.HashMap;

public class TestCommand extends BaseCommand {

    @Command("test")
    public void test(final Player player) {
        /*final FireworkBuilder builder = ItemBuilder.from(ItemType.FIREWORK_ROCKET).asFireworkBuilder();

        builder.addEffect(true, false, FireworkEffect.Type.BURST, List.of(
                Color.WHITE,
                Color.RED
        ), null).withDuration(5);

        builder.addItemToInventory(player.getInventory());*/

        final ItemBuilder builder = ItemBuilder.from(ItemType.STONE).withType("diamond_helmet").setItemDamage(50).addEnchantments(new HashMap<>() {{
            put("protection", 4);
            put("respiration", 1);
            put("aqua_affinity", 3);
            put("unbreaking", 3);
            put("thorns", 3);
        }}).setTrim("sentry", "quartz");

        player.getInventory().addItem(builder.asItemStack());
    }
}