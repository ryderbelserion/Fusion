package com.ryderbelserion.fusion.api;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.commands.CommandManager;
import com.ryderbelserion.fusion.commands.api.OriginCommand;
import com.ryderbelserion.fusion.commands.processor.RootProcessor;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PaperCommandManager extends CommandManager<CommandSourceStack> {

    private final LifecycleEventManager<Plugin> eventManager;

    public PaperCommandManager(@NotNull final JavaPlugin plugin) {
        this.eventManager = plugin.getLifecycleManager();
    }

    @Override
    public void init(@NotNull final String key) {
        if (!this.commands.containsKey(key)) return;

        final OriginCommand origin = this.commands.get(key);

        final RootProcessor processor = origin.getProcessor();

        final LiteralArgumentBuilder<CommandSourceStack> builder = processor.getBuilder();

        this.eventManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> event.registrar().register(builder.build(), processor.getDescription()));
    }
}