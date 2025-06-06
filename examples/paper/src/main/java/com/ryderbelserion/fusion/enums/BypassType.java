package com.ryderbelserion.fusion.enums;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public enum BypassType {

    allow_block_interact("allow_block_interact"),
    allow_item_drop("allow_item_drop"),
    allow_item_pickup("allow_item_pickup"),
    no_bypass("no_bypass");

    private final String chat;

    BypassType(@NotNull final String chat) {
        this.chat = chat;
    }

    public @NotNull final String getPrettyName() {
        return StringUtils.capitalize(getName().replace("_", " "));
    }

    public @NotNull final String getName() {
        return this.chat;
    }

    public static @NotNull BypassType getBypassType(@NotNull final String value) {
        BypassType type = BypassType.no_bypass;

        if (value.isEmpty()) {
            return type;
        }

        for (final BypassType key : BypassType.values()) {
            if (key.getName().equals(value)) {
                type = key;

                break;
            }
        }

        return type;
    }
}