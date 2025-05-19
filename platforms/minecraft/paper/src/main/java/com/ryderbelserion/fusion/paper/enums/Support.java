package com.ryderbelserion.fusion.paper.enums;

import com.ryderbelserion.fusion.paper.FusionPlugin;
import org.bukkit.plugin.Plugin;

public enum Support {

    placeholder_api("PlaceholderAPI"),
    head_database("HeadDatabase"),
    items_adder("ItemsAdder"),
    yard_watch("YardWatch"),
    oraxen("Oraxen"),
    nexo("Nexo");

    private final Plugin plugin = FusionPlugin.getPlugin();

    private final String name;

    Support(final String name) {
        this.name = name;
    }

    public final boolean isEnabled() {
        return this.plugin.getServer().getPluginManager().isPluginEnabled(this.name);
    }

    public final String getName() {
        return this.name;
    }
}