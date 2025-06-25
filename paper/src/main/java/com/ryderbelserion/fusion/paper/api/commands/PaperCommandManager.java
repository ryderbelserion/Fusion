package com.ryderbelserion.fusion.paper.api.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.common.api.commands.ICommandManager;
import com.ryderbelserion.fusion.paper.api.commands.objects.PaperCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PaperCommandManager extends ICommandManager<CommandSourceStack, PaperCommand> {

    private final Set<PaperCommand> commands = new HashSet<>();

    private final LifecycleEventManager<@NotNull Plugin> lifecycle;

    private final PluginManager pluginManager;

    public PaperCommandManager(@NotNull final JavaPlugin plugin) {
        this.lifecycle = plugin.getLifecycleManager();

        this.pluginManager = plugin.getServer().getPluginManager();
    }

    @Override
    public void enable(@NotNull final PaperCommand command, @Nullable final String description, @NotNull final List<String> aliases) {
        this.lifecycle.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands registry = event.registrar();

            registerPermissions(command.getPermissionDefault(), command.getPermissions());

            this.commands.add(command);

            final LiteralCommandNode<CommandSourceStack> root = command.build();

            command.getChildren().forEach(child -> {
                registerPermissions(child.getPermissionDefault(), child.getPermissions());

                this.commands.add(child);

                root.addChild(child.build());
            });

            registry.register(root, description, aliases);
        });
    }

    @Override
    public void disable() {
        for (final PaperCommand command : this.commands) {
            unregisterPermissions(command.getPermissions());
        }
    }

    public void unregisterPermissions(@NotNull final List<String> permissions) {
        for (final String node : permissions) {
            final Permission permission = this.pluginManager.getPermission(node);

            if (permission != null) {
                this.pluginManager.removePermission(permission);
            }
        }
    }

    public void registerPermissions(@NotNull final PermissionDefault permissionDefault, @NotNull final List<String> permissions) {
        for (final String node : permissions) {
            final Permission permission = this.pluginManager.getPermission(node);

            if (permission != null) {
                continue;
            }

            this.pluginManager.addPermission(new Permission(node, permissionDefault));
        }
    }
}