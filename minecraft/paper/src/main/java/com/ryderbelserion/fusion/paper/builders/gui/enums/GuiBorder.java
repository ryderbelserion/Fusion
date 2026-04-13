package com.ryderbelserion.fusion.paper.builders.gui.enums;

import org.jetbrains.annotations.NotNull;

public enum GuiBorder {

    TOP("top"),
    BOTTOM("bottom"),
    BORDER("border"),

    LEFT_SIDE("left"),
    RIGHT_SIDE("right"),
    BOTH_SIDES("side"),

    REMAINING_SLOTS("full");

    private final String type;

    GuiBorder(@NotNull final String type) {
        this.type = type;
    }

    public final String getType() {
        return this.type;
    }

    public static GuiBorder getFromName(@NotNull final String name) {
        GuiBorder origin = GuiBorder.REMAINING_SLOTS;

        for (final GuiBorder type : GuiBorder.values()) {
            if (type.getType().equalsIgnoreCase(name)) {
                origin = type;

                break;
            }
        }

        return origin;
    }
}