package com.ryderbelserion.fusion.mojang.context;

import com.mojang.brigadier.context.CommandContext;
import org.jspecify.annotations.NonNull;
import java.util.Optional;

public abstract class AbstractCommandContext<S> {

    protected final CommandContext<S> context;

    public AbstractCommandContext(@NonNull final CommandContext<S> context) {
        this.context = context;
    }

    public @NonNull final Optional<String> getStringArgument(@NonNull final String key) {
        return Optional.ofNullable(this.context.getArgument(key, String.class));
    }

    public final Optional<Double> getDoubleArgument(@NonNull final String key) {
        return Optional.ofNullable(this.context.getArgument(key, Double.class));
    }

    public final Optional<Integer> getIntegerArgument(@NonNull final String key) {
        return Optional.ofNullable(this.context.getArgument(key, Integer.class));
    }

    public final Optional<Float> getFloatArgument(@NonNull final String key) {
        return Optional.ofNullable(this.context.getArgument(key, Float.class));
    }

    public @NonNull final CommandContext<S> getContext() {
        return this.context;
    }

    public @NonNull final S getSource() {
        return this.context.getSource();
    }
}