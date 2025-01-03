package com.ryderbelserion.fusion.commands;

import com.ryderbelserion.core.commands.CommandManager;
import com.ryderbelserion.fusion.FusionPlugin;
import com.ryderbelserion.fusion.commands.types.HelpCommand;
import java.util.List;

public class FusionManager {

    private static final FusionPlugin plugin = FusionPlugin.getPlugin();

    private static final CommandManager instance = new CommandManager();

    public static void load() {
        //List.of(
        //        new HelpCommand()
        //).forEach(instance::registerCommand);
    }
}