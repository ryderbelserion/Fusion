package com.ryderbelserion.fusion.paper.api.enums;

import com.ryderbelserion.fusion.paper.FusionPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public enum Support {

    oraxen("Oraxen"),
    nexo("Nexo"),
    items_adder("ItemsAdder"),
    head_database("HeadDatabase"),
    placeholder_api("PlaceholderAPI"),
    yard_watch("YardWatch");

    private final Plugin plugin = FusionPlugin.getPlugin();

    private final PluginManager server = this.plugin.getServer().getPluginManager();

    private final String name;

    Support(@NotNull final String name) {
        this.name = name;
    }

    public final boolean isEnabled() {
        return this.server.isPluginEnabled(this.name);
    }

    public @NotNull final String getName() {
        return this.name;
    }
}