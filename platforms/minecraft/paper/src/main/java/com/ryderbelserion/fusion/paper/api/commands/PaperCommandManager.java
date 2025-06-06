package com.ryderbelserion.fusion.paper.api.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.kyori.commands.CommandManager;
import com.ryderbelserion.fusion.kyori.enums.PermissionMode;
import com.ryderbelserion.fusion.paper.api.commands.objects.AbstractPaperCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PaperCommandManager extends CommandManager<CommandSourceStack, AbstractPaperCommand> {

    private final Set<AbstractPaperCommand> commands = new HashSet<>();

    private final LifecycleEventManager<@NotNull Plugin> lifecycle;

    private final PluginManager pluginManager;

    public PaperCommandManager(@NotNull final JavaPlugin plugin) {
        this.lifecycle = plugin.getLifecycleManager();

        this.pluginManager = plugin.getServer().getPluginManager();
    }

    @Override
    public final boolean hasPermission(@NotNull final CommandSourceStack stack, @NotNull final PermissionMode mode, @NotNull final String[] permissions) {
        final CommandSender sender = stack.getSender();

        boolean hasPermission = !(sender instanceof Player) || sender.isOp();

        if (hasPermission) {
            return true;
        }

        final Iterator<String> iterator = Arrays.stream(permissions).iterator();

        while (iterator.hasNext()) {
            final String permission = iterator.next();

            if (mode == PermissionMode.ANY_OF) {
                hasPermission = sender.hasPermission(permission);

                break;
            }

            hasPermission = sender.hasPermission(permission);

            if (!hasPermission) {
                break;
            }
        }

        return hasPermission;
    }

    @Override
    public void enable(@NotNull final AbstractPaperCommand command) {
        this.lifecycle.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands registry = event.registrar();

            final LiteralCommandNode<CommandSourceStack> root = command.build();

            command.getChildren().forEach(child -> root.addChild(child.build()));

            registry.register(root);
        });
    }

    public void unregisterPermissions(@NotNull final String[] permissions) {
        final Iterator<String> iterator = Arrays.stream(permissions).iterator();

        while (iterator.hasNext()) {
            final String node = iterator.next();

            final Permission permission = this.pluginManager.getPermission(node);

            if (permission != null) {
                this.pluginManager.removePermission(permission);
            }
        }
    }

    public void registerPermissions(@NotNull final PermissionDefault permissionDefault, @NotNull final String[] permissions) {
        final Iterator<String> iterator = Arrays.stream(permissions).iterator();

        while (iterator.hasNext()) {
            final String node = iterator.next();

            final Permission permission = this.pluginManager.getPermission(node);

            if (permission != null) {
                continue;
            }

            this.pluginManager.addPermission(new Permission(node, permissionDefault));
        }
    }
}