package com.ryderbelserion.fusion.mojang.context;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedArgument;
import org.jspecify.annotations.NonNull;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractCommandContext<S> {

    protected final Map<String, ParsedArgument<S, ?>> arguments;
    protected final CommandContext<S> context;

    public AbstractCommandContext(@NonNull final CommandContext<S> context) {
        this.context = context;

        this.arguments = new HashMap<>();
    }

    public @NonNull final Map<String, ParsedArgument<S, ?>> getArguments() {
        Map<String, ParsedArgument<S, ?>> map = new HashMap<>();

        final Class<?> object = this.context.getClass();

        try {
            final Field field = object.getDeclaredField("arguments");

            field.trySetAccessible();

            map = (Map<String, ParsedArgument<S, ?>>) field.get(this.context);
        } catch (final NoSuchFieldException | IllegalAccessException exception) {
            return map;
        }

        return Collections.unmodifiableMap(map);
    }

    public final boolean hasArgument(@NonNull final String key) {
        return getArguments().containsKey(key);
    }

    public @NonNull final Optional<String> getStringArgument(@NonNull final String key) {
        if (!hasArgument(key)) {
            return Optional.empty();
        }

        return Optional.ofNullable(this.context.getArgument(key, String.class));
    }

    public final Optional<Double> getDoubleArgument(@NonNull final String key) {
        if (!hasArgument(key)) {
            return Optional.empty();
        }

        return Optional.ofNullable(this.context.getArgument(key, Double.class));
    }

    public final Optional<Integer> getIntegerArgument(@NonNull final String key) {
        if (!hasArgument(key)) {
            return Optional.empty();
        }

        return Optional.ofNullable(this.context.getArgument(key, Integer.class));
    }

    public final Optional<Float> getFloatArgument(@NonNull final String key) {
        if (!hasArgument(key)) {
            return Optional.empty();
        }

        return Optional.ofNullable(this.context.getArgument(key, Float.class));
    }

    public @NonNull final CommandContext<S> getContext() {
        return this.context;
    }

    public @NonNull final S getSource() {
        return this.context.getSource();
    }
}