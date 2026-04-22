package com.ryderbelserion.fusion.mojang.context;

import com.mojang.brigadier.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommandContext<S> {

    protected final CommandContext<S> context;

    protected final List<String> args = new ArrayList<>();

    public AbstractCommandContext(@NotNull final CommandContext<S> context) {
        this.context = context;
    }

    public boolean hasArgument(@NotNull final String name, @NotNull final Class<?> clazz) {
        final Object argument = this.context.getArgument(name, clazz);

        return argument != null;
    }

    public boolean hasIntegerArgument(@NotNull final String name) {
        return hasArgument(name, Integer.class);
    }

    public boolean hasDoubleArgument(@NotNull final String name) {
        return hasArgument(name, Double.class);
    }

    public boolean hasStringArgument(@NotNull final String name) {
        return hasArgument(name, String.class);
    }

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

    public void addArgument(@NotNull final String key) {
        this.args.add(key);
    }

    public boolean hasArgument(@NotNull final String key) {
        return this.args.contains(key);
    }

    public @NotNull final CommandContext<S> getContext() {
        return this.context;
    }

    public @NotNull final S getSource() {
        return this.context.getSource();
    }
}