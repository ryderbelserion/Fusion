package com.ryderbelserion.fusion.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.Fusion;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.ryderbelserion.fusion.kyori.permissions.enums.PermissionType;
import com.ryderbelserion.fusion.paper.builders.commands.PaperCommand;
import com.ryderbelserion.fusion.paper.builders.commands.context.PaperCommandContext;
import com.ryderbelserion.fusion.paper.builders.gui.enums.GuiBorder;
import com.ryderbelserion.fusion.paper.builders.gui.enums.GuiState;
import com.ryderbelserion.fusion.paper.builders.gui.objects.GuiItem;
import com.ryderbelserion.fusion.paper.builders.gui.types.paginated.PaginatedGui;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class SimpleCommand extends PaperCommand {

    private final Fusion fusion;

    public SimpleCommand(@NotNull final Fusion fusion) {
        this.fusion = fusion;
    }

    @Override
    public void run(@NotNull final PaperCommandContext context) {
        final CommandSender sender = context.getSender();

        if (!(sender instanceof Player player)) return;

        final PaginatedGui gui = PaginatedGui.gui(
                this.fusion,
                player,
                "<red>Example Gui",
                4
        );

        gui.getFiller().fill(GuiBorder.BORDER, ItemType.BLACK_STAINED_GLASS_PANE.createItemStack());

        gui.addPageItem(new GuiItem(ItemType.RED_TERRACOTTA.createItemStack()));

        //gui.setPageItem(13, ItemType.CHEST.createItemStack());

        gui.addState(GuiState.block_all_interactions);

        gui.open(player);
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("fusion").requires(this::requirement)
                .executes(context -> {
                    run(new PaperCommandContext(context));

                    return Command.SINGLE_SUCCESS;
                }).build();
    }

    @Override
    public @NotNull final List<PermissionContext> getPermissions() {
        return List.of(
                new PermissionContext(
                        "fusion.use",
                        "The base command for Fusion!",
                        PermissionType.TRUE
                )
        );
    }

    @Override
    public final boolean requirement(@NotNull final CommandSourceStack context) {
        return context.getSender().hasPermission(getPermissions().getFirst().getPermission());
    }
}