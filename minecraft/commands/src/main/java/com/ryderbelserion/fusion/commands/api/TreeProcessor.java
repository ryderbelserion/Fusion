package com.ryderbelserion.fusion.commands.api;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.commands.CommandManager;
import com.ryderbelserion.fusion.commands.api.annotations.other.Permission;
import com.ryderbelserion.fusion.commands.api.annotations.subs.Branch;
import com.ryderbelserion.fusion.commands.api.annotations.Flower;
import com.ryderbelserion.fusion.commands.api.annotations.Tree;
import com.ryderbelserion.fusion.commands.api.annotations.subs.Leaf;
import com.ryderbelserion.fusion.commands.api.objects.LeafCommand;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class TreeProcessor<S> {

    private LiteralArgumentBuilder<S> builder;
    private String description;
    private String tree;

    public @NotNull List<LeafCommand<S>> processTree(@NotNull final CommandManager<S> commandManager, @NotNull final Method[] methods) {
        return Arrays.stream(methods)
                .filter(insect -> insect.isAnnotationPresent(Leaf.class))
                .sorted(Comparator.comparingInt(insect -> insect.getAnnotation(Leaf.class).weight()))
                .map(map -> new LeafCommand<>(commandManager, map, map.getAnnotation(Leaf.class)))
                .toList();
    }

    public @NotNull TreeProcessor process(@NotNull final CommandManager<S> commandManager, @NotNull final Object object) {
        final Class<?> root = object.getClass();

        if (root.isAnnotationPresent(Tree.class)) {
            final Tree tree = root.getAnnotation(Tree.class);

            this.builder = LiteralArgumentBuilder.literal(this.tree = tree.value());

            final String permission = root.isAnnotationPresent(Permission.class) ? root.getAnnotation(Permission.class).permission() : "";

            if (!permission.isBlank()) {
                builder.requires(context -> commandManager.hasPermission(context, permission));
            }

            final List<Method> methods = filter(root, insect -> insect.isAnnotationPresent(Flower.class));

            if (!methods.isEmpty()) {
                this.builder.executes(_ -> invoke(methods.getFirst(), object));
            }

            this.description = tree.description();

            return this;
        }

        if (root.isAnnotationPresent(Branch.class)) {
            final Branch branch = root.getAnnotation(Branch.class);

            final LiteralArgumentBuilder<S> builder = LiteralArgumentBuilder.literal(branch.value());

            final String permission = root.isAnnotationPresent(Permission.class) ? root.getAnnotation(Permission.class).permission() : "";

            if (!permission.isBlank()) {
                builder.requires(context -> commandManager.hasPermission(context, permission));
            }

            final List<Method> methods = filter(root, insect -> insect.isAnnotationPresent(Flower.class));

            if (!methods.isEmpty()) {
                builder.executes(_ -> invoke(methods.getFirst(), object));
            }

            for (final LeafCommand leaf : processTree(commandManager, root.getDeclaredMethods())) {
                builder.then(leaf.execute(object));
            }

            this.builder = this.builder.then(builder);
        }

        return this;
    }

    public int invoke(@NotNull final Method method, @NotNull final Object object) {
        if (!method.trySetAccessible()) return Command.SINGLE_SUCCESS;

        try {
            method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }

        return Command.SINGLE_SUCCESS;
    }

    public @NotNull List<Method> filter(@NotNull final Class<?> tree, @NotNull final Predicate<? super Method> predicate) {
        return Arrays.stream(tree.getDeclaredMethods()).filter(predicate).toList();
    }

    public @NotNull final LiteralArgumentBuilder<S> getBuilder() {
        return this.builder;
    }

    public @NotNull final String getDescription() {
        return this.description;
    }

    public @NotNull final String getTree() {
        return this.tree;
    }
}