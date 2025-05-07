package com.ryderbelserion.fusion.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import com.ryderbelserion.fusion.FusionPlugin;
import com.ryderbelserion.fusion.commands.brigadier.ExampleCommand;
import com.ryderbelserion.fusion.commands.brigadier.annotations.Command;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

public class CommandManager {

    private static final Logger log = LoggerFactory.getLogger(CommandManager.class);
    private final FusionPlugin plugin;

    public CommandManager(final FusionPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        final LifecycleEventManager<@NotNull Plugin> lifeCycle = this.plugin.getLifecycleManager();

        final ExampleCommand command = new ExampleCommand();

        final Command annotation = command.getClass().getAnnotation(Command.class);

        if (annotation == null) return;

        final ComponentLogger logger = this.plugin.getComponentLogger();

        logger.warn("Annotation: {}", annotation.value());

        AtomicReference<CommandNode<CommandSourceStack>> parent = new AtomicReference<>();

        final String value = annotation.value();

        lifeCycle.registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            final CommandDispatcher<CommandSourceStack> dispatcher = commands.registrar().getDispatcher();

            final RootCommandNode<CommandSourceStack> root = dispatcher.getRoot();

            commands.registrar().register(Commands.literal(value).build());

            logger.warn("Name: {}", root.getName());

            //parent.set(root.getChild(value));
        });

        final CommandNode<CommandSourceStack> root = parent.get();

        if (root == null) return;

        logger.warn("Name: {}", root.getName());

        for (final Method method : command.getClass().getMethods()) {
            final Command subCommand  = method.getAnnotation(Command.class);

            if (subCommand == null) continue;

            logger.warn("Subcommand: {}", subCommand.value());
        }
    }
}