package com.ryderbelserion.commands.brigadier.types.admin;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.paper.api.builders.items.ItemBuilder;
import com.ryderbelserion.fusion.paper.api.commands.objects.PaperCommand;
import com.ryderbelserion.fusion.paper.api.commands.objects.PaperCommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemType;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class CommandItem extends PaperCommand {

    @Override
    public void execute(@NotNull final PaperCommandContext context) {
        final Player player = context.getPlayer();

        final ItemBuilder itemBuilder = ItemBuilder.from(ItemType.POTION);

        itemBuilder.asPotionBuilder().withPotionEffect(
                PotionEffectType.ABSORPTION,
                600,
                3
        ).setColor("green").build();

        itemBuilder.addItemToInventory(player.getInventory());
    }

    @Override
    public final boolean requirement(@NotNull final CommandSourceStack source) {
        final CommandSender sender = source.getSender();

        return sender instanceof Player;
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("item")
                .requires(this::requirement)
                .executes(context -> {
                    execute(new PaperCommandContext(context));

                    return Command.SINGLE_SUCCESS;
                }).build();
    }

    @Override
    public @NotNull final List<String> getPermissions() {
        return List.of("crazycrates.reload");
    }
}