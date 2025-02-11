package com.ryderbelserion.core.api.commands.context;

import org.jetbrains.annotations.NotNull;

public abstract class CommandContext<S> {

    private final com.mojang.brigadier.context.CommandContext<S> context;

    public CommandContext(@NotNull final com.mojang.brigadier.context.CommandContext<S> context) {
        this.context = context;
    }

    public @NotNull final S getSource() {
        return this.context.getSource();
    }

    public @NotNull final String getStringArgument(@NotNull final String key) {
        return this.context.getArgument(key, String.class);
    }

    public final int getIntegerArgument(@NotNull final String key) {
        return this.context.getArgument(key, Integer.class);
    }

    public final float getFloatArgument(@NotNull final String key) {
        return this.context.getArgument(key, Float.class);
    }

    public final double getDoubleArgument(@NotNull final String key) {
        return this.context.getArgument(key, Double.class);
    }
}