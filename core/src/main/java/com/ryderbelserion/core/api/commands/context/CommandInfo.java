package com.ryderbelserion.core.api.commands.context;

import com.mojang.brigadier.context.CommandContext;
import org.jetbrains.annotations.NotNull;

/**
 * An abstract class for command information.
 * This class encapsulates the context and provides methods to retrieve command arguments of various types.
 *
 * @param <S> the command source stack
 *
 * @author Ryder Belserion
 * @version 0.9.0
 * @since 0.9.0
 */
public abstract class CommandInfo<S> {

    private final CommandContext<S> context;

    /**
     * Constructs a CommandInfo with the specified context.
     *
     * @param context the command context
     * @since 0.9.0
     */
    public CommandInfo(@NotNull final CommandContext<S> context) {
        this.context = context;
    }

    /**
     * Gets the command source stack.
     *
     * @return the command source stack
     * @since 0.9.0
     */
    public @NotNull final S getSource() {
        return this.context.getSource();
    }

    /**
     * Gets the string argument value for the specified key.
     *
     * @param key the argument name
     * @return the argument value as a String
     * @since 0.9.0
     */
    public @NotNull final String getStringArgument(@NotNull final String key) {
        return this.context.getArgument(key, String.class);
    }

    /**
     * Gets the integer argument value for the specified key.
     *
     * @param key the argument name
     * @return the argument value as an Integer
     * @since 0.9.0
     */
    public final int getIntegerArgument(@NotNull final String key) {
        return this.context.getArgument(key, Integer.class);
    }

    /**
     * Gets the float argument value for the specified key.
     *
     * @param key the argument name
     * @return the argument value as a Float
     * @since 0.9.0
     */
    public final float getFloatArgument(@NotNull final String key) {
        return this.context.getArgument(key, Float.class);
    }

    /**
     * Gets the double argument value for the specified key.
     *
     * @param key the argument name
     * @return the argument value as a Double
     * @since 0.9.0
     */
    public final double getDoubleArgument(@NotNull final String key) {
        return this.context.getArgument(key, Double.class);
    }
}