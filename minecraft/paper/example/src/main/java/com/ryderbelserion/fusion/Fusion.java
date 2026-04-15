package com.ryderbelserion.fusion;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.commands.SimpleCommand;import com.ryderbelserion.fusion.paper.FusionPaper;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Fusion extends JavaPlugin implements Listener {

    private FusionPaper fusion;

    @Override
    public void onEnable() {
        this.fusion = new FusionPaper(this);
        this.fusion.init();

        final LifecycleEventManager<Plugin> eventManager = getLifecycleManager();

        // Register commands.
        eventManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            LiteralArgumentBuilder<CommandSourceStack> root = new SimpleCommand(this).registerPermissions().literal().createBuilder();

            event.registrar().register(root.build(), "The base command for Fusion!");
        });
    }

    public @NotNull final FusionPaper getFusion() {
        return this.fusion;
    }
}