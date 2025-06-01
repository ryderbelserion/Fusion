package com.ryderbelserion.fusion.commands;

import com.ryderbelserion.fusion.Fusion;
import com.ryderbelserion.fusion.commands.types.CommandItem;
import com.ryderbelserion.fusion.commands.types.CommandReload;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;

public class CommandHandler {

    private final Fusion plugin = JavaPlugin.getPlugin(Fusion.class);

    private final BukkitCommandManager<CommandSender> commandManager = BukkitCommandManager.create(this.plugin);

    public CommandHandler() {
        load();
    }

    public void load() {
        List.of(
                new CommandReload(),
                new CommandItem()
        ).forEach(this.commandManager::registerCommand);
    }

    public final BukkitCommandManager<CommandSender> getCommandManager() {
        return this.commandManager;
    }
}