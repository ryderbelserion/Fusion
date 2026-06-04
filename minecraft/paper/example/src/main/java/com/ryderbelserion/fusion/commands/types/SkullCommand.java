package com.ryderbelserion.fusion.commands.types;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.Fusion;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.ryderbelserion.fusion.kyori.permissions.enums.PermissionType;
import com.ryderbelserion.fusion.paper.builders.commands.PaperCommand;
import com.ryderbelserion.fusion.paper.builders.commands.context.PaperCommandContext;
import com.ryderbelserion.fusion.paper.builders.items.ItemBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.NonNull;
import java.util.List;

public class SkullCommand extends PaperCommand {

    private final Fusion fusion;

    public SkullCommand(@NonNull final Fusion fusion) {
        this.fusion = fusion;
    }

    @Override
    public void run(@NonNull final PaperCommandContext context) {
        final CommandSender sender = context.getSender();

        if (!(sender instanceof Player player)) {
            sender.sendRichMessage("<red>You must be a player to run this command!");

            return;
        }

        final ItemBuilder builder = ItemBuilder.from(ItemType.PLAYER_HEAD);

        builder.asSkullBuilder().withName("1ee3126ff2c343da525eef2b93272b9fed36273d0ea08c2616b80009948ad57e").build();

        player.getInventory().addItem(builder.asItemStack(player));
    }

    @Override
    public @NonNull final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("skull").requires(this::requirement)
                .executes(context -> {
                    run(new PaperCommandContext(context));

                    return Command.SINGLE_SUCCESS;
                }).build();
    }

    @Override
    public @NonNull final List<PermissionContext> getPermissions() {
        return List.of(
                new PermissionContext(
                        "fusion.use",
                        "The base command for Fusion!",
                        PermissionType.TRUE
                )
        );
    }

    @Override
    public final boolean requirement(@NonNull final CommandSourceStack context) {
        return context.getSender().hasPermission(getPermissions().getFirst().getPermission());
    }
}