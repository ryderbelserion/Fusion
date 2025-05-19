package com.ryderbelserion.fusion.paper;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class FusionPlugin {

    private static Plugin plugin;

    public static void setPlugin(@NotNull final Plugin plugin) {
        FusionPlugin.plugin = plugin;
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}