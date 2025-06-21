package com.ryderbelserion.fusion.kyori.enums;

import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import org.jetbrains.annotations.NotNull;

public enum Support {

    placeholder_api("PlaceholderAPI"),
    head_database("HeadDatabase"),
    items_adder("ItemsAdder"),
    yard_watch("YardWatch"),
    oraxen("Oraxen"),
    nexo("Nexo");

    private final FusionKyori kyori = (FusionKyori) FusionProvider.get();

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