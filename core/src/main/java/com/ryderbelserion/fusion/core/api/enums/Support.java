package com.ryderbelserion.fusion.core.api.enums;

import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.api.FusionCommon;
import org.jetbrains.annotations.NotNull;

public enum Support {

    placeholder_api("PlaceholderAPI"),
    head_database("HeadDatabase"),
    items_adder("ItemsAdder"),
    yard_watch("YardWatch"),
    oraxen("Oraxen"),
    nexo("Nexo");

    private final FusionCommon fusion = FusionProvider.get();

    private final String name;

    Support(@NotNull final String name) {
        this.name = name;
    }

    public final boolean isEnabled() {
        return this.fusion.isPluginEnabled(this.name);
    }

    public @NotNull final String getName() {
        return this.name;
    }
}