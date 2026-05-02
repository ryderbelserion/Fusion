package com.ryderbelserion.fusion.commands.processor;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.commands.annotations.Origin;
import com.ryderbelserion.fusion.commands.annotations.Leaf;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class RootProcessor<S> {

    private LiteralArgumentBuilder<S> builder;
    private String description;
    private String origin;

    public @NotNull List<Leaf> processTree(@NotNull final Method[] methods) {
        return Arrays.stream(methods).filter(insect -> insect.isAnnotationPresent(Leaf.class)).sorted(Comparator.comparingInt(insect -> insect.getAnnotation(Leaf.class).weight())).map(map -> map.getAnnotation(Leaf.class)).toList();
    }

    public @NotNull RootProcessor process(@NotNull final Object object) {
        final Class<?> root = object.getClass();

        if (root.isAnnotationPresent(Origin.class)) {
            final Origin origin = root.getAnnotation(Origin.class);

            this.builder = LiteralArgumentBuilder.literal(this.origin = origin.value());
            this.description = origin.description();

            return this;
        }

        if (root.isAnnotationPresent(Leaf.class)) {
            final Leaf origin = root.getAnnotation(Leaf.class);

            final String value = origin.value();

            LiteralArgumentBuilder<S> builder = LiteralArgumentBuilder.literal(value);

            for (final Leaf leaf : processTree(root.getDeclaredMethods())) {
                builder.then(LiteralArgumentBuilder.literal(leaf.value()));
            }

            this.builder = this.builder.then(LiteralArgumentBuilder.literal(value)).then(builder);
        }

        return this;
    }

    public @NotNull final LiteralArgumentBuilder<S> getBuilder() {
        return this.builder;
    }

    public @NotNull final String getDescription() {
        return this.description;
    }

    public @NotNull final String getOrigin() {
        return this.origin;
    }
}