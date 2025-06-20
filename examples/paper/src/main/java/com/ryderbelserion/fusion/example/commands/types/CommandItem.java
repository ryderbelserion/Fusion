package com.ryderbelserion.fusion.example.commands.types;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.example.Fusion;
import com.ryderbelserion.fusion.paper.api.builders.items.ItemBuilder;
import com.ryderbelserion.fusion.paper.api.builders.items.types.SpawnerBuilder;
import com.ryderbelserion.fusion.paper.api.commands.objects.AbstractPaperCommand;
import com.ryderbelserion.fusion.paper.api.commands.objects.AbstractPaperContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemType;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class CommandItem extends AbstractPaperCommand {

    private final Fusion plugin = JavaPlugin.getPlugin(Fusion.class);

    @Override
    public void execute(@NotNull final AbstractPaperContext context) {
        if (!context.isPlayer()) {
            context.getCommandSender().sendRichMessage("<red>You must be a player to use this command!</red>");

            return;
        }

        final Player player = context.getPlayer();

        final ItemBuilder itemBuilder = ItemBuilder.from(ItemType.DIAMOND_SWORD).setDisplayName("<red>{name}, <yes>").addEnchantment("sharpness", 3).hideComponent("enchantments")
                .addPlaceholder("<yes>", "this is yes")
                .addPlaceholder("{name}", player.getName());

        itemBuilder.addItemToInventory(player.getInventory());

        final @NotNull SpawnerBuilder spawnerItem = ItemBuilder.from(ItemType.SPAWNER).asSpawnerBuilder();

        spawnerItem.withEntityType(EntityType.CREEPER).withSpawnCount(10).withSpawnDelay(20).withSpawnRange(5).build().addItemToInventory(player.getInventory());

        player.sendRichMessage("<green>Item added to inventory!");
    }

    @Override
    public final boolean requirement(@NotNull final CommandSourceStack source) {
        return source.getSender().hasPermission(getPermissions().getFirst());
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("item")
                .requires(this::requirement)
                .executes(context -> {
                    execute(new AbstractPaperContext(context));

                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                }).build();
    }

    @Override
    public @NotNull final PermissionDefault getPermissionDefault() {
        return PermissionDefault.OP;
    }

    @Override
    public @NotNull final List<String> getPermissions() {
        return List.of("fusion.item");
    }
}