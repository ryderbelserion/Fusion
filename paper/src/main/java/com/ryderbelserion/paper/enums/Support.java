package com.ryderbelserion.paper.enums;

import com.ryderbelserion.paper.FusionApi;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public enum Support {

    oraxen("Oraxen"),
    nexo("Nexo"),
    items_adder("ItemsAdder"),
    head_database("HeadDatabase"),
    placeholder_api("PlaceholderAPI");

    private final Plugin plugin = FusionApi.get().getPlugin();

    private final String name;

    Support(@NotNull final String name) {
        this.name = name;
    }

    public final boolean isEnabled() {
        return this.plugin.getServer().getPluginManager().isPluginEnabled(this.name);
    }

    public @NotNull final String getName() {
        return this.name;
    }
}