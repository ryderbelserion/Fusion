package com.ryderbelserion.fusion.kyori.enums;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import org.jetbrains.annotations.NotNull;

public enum Support {

    oraxen("Oraxen"),
    nexo("Nexo"),
    items_adder("ItemsAdder"),
    head_database("HeadDatabase"),
    placeholder_api("PlaceholderAPI"),
    yard_watch("YardWatch");

    private final FusionKyori kyori = (FusionKyori) FusionCore.Provider.get();

    private final String name;

    Support(@NotNull final String name) {
        this.name = name;
    }

    public final boolean isEnabled() {
        return this.kyori.isPluginEnabled(this.name);
    }

    public @NotNull final String getName() {
        return this.name;
    }
}