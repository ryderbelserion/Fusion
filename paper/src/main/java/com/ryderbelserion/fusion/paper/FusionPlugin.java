package com.ryderbelserion.fusion.paper;

import org.bukkit.plugin.Plugin;

public class FusionPlugin {

    private static Plugin plugin;

    public static void setPlugin(final Plugin plugin) {
        FusionPlugin.plugin = plugin;
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}