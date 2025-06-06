package com.ryderbelserion.fusion.paper.api.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.kyori.commands.CommandManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class PaperCommandManager extends CommandManager<CommandSourceStack> {

    private final LifecycleEventManager<@NotNull Plugin> lifecycle;

    public PaperCommandManager(@NotNull final JavaPlugin plugin) {
        this.lifecycle = plugin.getLifecycleManager();
    }

    @Override
    public void enable(@NotNull final LiteralCommandNode<CommandSourceStack> root, @NotNull final List<LiteralCommandNode<CommandSourceStack>> children) {
        this.lifecycle.registerEventHandler(LifecycleEvents.COMMANDS, command -> {
            final Commands registry = command.registrar();

            children.forEach(root::addChild);

            registry.register(root);
        });
    }
}