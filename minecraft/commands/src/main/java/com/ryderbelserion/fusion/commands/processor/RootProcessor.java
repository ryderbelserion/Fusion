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

    public @NotNull RootProcessor processTree(@NotNull final Method[] methods) {
        final List<Method> trees = Arrays.stream(methods).filter(insect -> insect.isAnnotationPresent(Leaf.class)).sorted(Comparator.comparingInt(insect -> insect.getAnnotation(Leaf.class).weight())).toList();

        if (trees.isEmpty()) return this;

        for (final Method tree : trees) {
            final Leaf leaf = tree.getAnnotation(Leaf.class);

            final String value = leaf.value();

            if (value.isBlank()) continue;

            this.builder = this.builder.then(LiteralArgumentBuilder.literal(value));
        }

        return this;
    }

    public @NotNull RootProcessor process(@NotNull final Object object) {
        final Class<?> root = object.getClass();

        if (root.isAnnotationPresent(Origin.class)) {
            final Origin origin = root.getAnnotation(Origin.class);

            final String value = origin.value();

            if (this.builder == null) {
                this.builder = LiteralArgumentBuilder.literal(this.origin = value);
            }

            this.description = origin.description();

            return this;
        }

        if (root.isAnnotationPresent(Leaf.class)) {
            final Leaf origin = root.getAnnotation(Leaf.class);

            final String value = origin.value();

            this.builder = this.builder.then(LiteralArgumentBuilder.literal(value));

            processTree(object.getClass().getDeclaredMethods());
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