package com.ryderbelserion.fusion.commands.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.FusionPlugin;
import com.ryderbelserion.paper.commands.PaperCommand;
import com.ryderbelserion.paper.commands.context.PaperCommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class RootCommand extends PaperCommand {

    protected final FusionPlugin plugin = FusionPlugin.getPlugin();

    @Override
    public void execute(final PaperCommandContext info) {

    }

    @Override
    public boolean requirement(final CommandSourceStack context) {
        return context.getSender().hasPermission(getPermission());
    }

    @Override
    public @NotNull final String getPermission() {
        return "redstonepvp.access";
    }

    @Override
    public @NotNull LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("redstonepvp")
                .requires(this::requirement)
                .executes(context -> {
                    execute(new PaperCommandContext(context));

                    return Command.SINGLE_SUCCESS;
                }).build();
    }

    @Override
    public @NotNull final PaperCommand registerPermission() {
        final PluginManager manager = this.plugin.getServer().getPluginManager();

        final String node = getPermission();

        final Permission permission = manager.getPermission(node);

        if (permission == null) {
            manager.addPermission(new Permission(node, PermissionDefault.OP));
        }

        return this;
    }
}