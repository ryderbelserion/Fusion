package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.commands.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FusionPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        new CommandManager(this).load();
    }
}