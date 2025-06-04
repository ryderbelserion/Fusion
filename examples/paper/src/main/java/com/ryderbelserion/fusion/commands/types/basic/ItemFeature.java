package com.ryderbelserion.fusion.commands.types.basic;

import com.ryderbelserion.fusion.commands.AnnotationFeature;
import com.ryderbelserion.fusion.paper.api.builders.items.ItemBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemType;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;
import org.jetbrains.annotations.NotNull;

public class ItemFeature extends AnnotationFeature {

    @Override
    public void registerFeature(@NotNull final AnnotationParser<CommandSourceStack> parser) {
        parser.parse(this);
    }

    @Command("fusion item")
    @CommandDescription("Gives a player an item!")
    @Permission(value = "fusion.item", mode = Permission.Mode.ALL_OF)
    public void item(final Player player) {
        final ItemBuilder itemBuilder = ItemBuilder.from(ItemType.DIAMOND_SWORD).setDisplayName("<red>{name}, <yes>").addEnchantment("sharpness", 3).hideComponent("enchantments")
                .addPlaceholder("<yes>", player.getUniqueId().toString())
                .addPlaceholder("{name}", player.getName());

        itemBuilder.addItemToInventory(player.getInventory());
    }
}