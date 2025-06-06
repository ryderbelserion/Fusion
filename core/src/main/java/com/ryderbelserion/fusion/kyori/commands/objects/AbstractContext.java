package com.ryderbelserion.fusion.kyori.commands.objects;

import com.mojang.brigadier.context.CommandContext;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractContext<S, P> {

    private final CommandContext<S> context;

    public AbstractContext(@NotNull final CommandContext<S> context) {
        this.context = context;
    }

    public abstract P getPlayer();

    public abstract boolean isPlayer();

    public @NotNull final String getStringArgument(@NotNull final String key) {
        return this.context.getArgument(key, String.class);
    }

    public final double getDoubleArgument(@NotNull final String key) {
        return this.context.getArgument(key, Double.class);
    }

    public final int getIntegerArgument(@NotNull final String key) {
        return this.context.getArgument(key, Integer.class);
    }

    public final float getFloatArgument(@NotNull final String key) {
        return this.context.getArgument(key, Float.class);
    }

    public CommandContext<S> getContext() {
        return this.context;
    }

    public final S getSource() {
        return this.context.getSource();
    }
}