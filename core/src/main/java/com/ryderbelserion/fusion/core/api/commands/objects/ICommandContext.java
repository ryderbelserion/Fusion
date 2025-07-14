package com.ryderbelserion.fusion.core.api.commands.objects;

import com.mojang.brigadier.context.CommandContext;
import org.jetbrains.annotations.NotNull;

/**
 * Builds the command context.
 *
 * @param <S> the platform's CommandSourceStack, or otherwise the sender.
 * @param <P> the platform's player object.
 */
public abstract class ICommandContext<S, P> {

    private final CommandContext<S> context;

    public ICommandContext(@NotNull final CommandContext<S> context) {
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

    public S getSource() {
        return this.context.getSource();
    }
}