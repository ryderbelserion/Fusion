package com.ryderbelserion.fusion.commands.api.objects.types;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.commands.CommandManager;
import com.ryderbelserion.fusion.commands.api.annotations.other.Permission;
import com.ryderbelserion.fusion.commands.api.annotations.subs.Leaf;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class LeafCommand<S> {

    private final CommandManager<S> manager;
    private final Parameter[] parameters;
    private final String permission;
    private final Method method;
    private final String leaf;

    public LeafCommand(@NotNull final CommandManager<S> manager, @NotNull final Method method, @NotNull final Leaf leaf) {
        this.permission = method.isAnnotationPresent(Permission.class) ? method.getAnnotation(Permission.class).permission() : "";
        this.leaf = leaf.value();
        this.manager = manager;
        this.method = method;

        this.parameters = method.getParameters();
    }

    public @NotNull final LiteralArgumentBuilder<S> execute(@NotNull final Object object) {
        final LiteralArgumentBuilder<S> builder = LiteralArgumentBuilder.literal(this.leaf);

        if (!this.permission.isBlank()) {
            builder.requires(context -> this.manager.hasPermission(context, this.permission));
        }

        builder.executes(_ -> invoke(this.method, object));

        return builder;
    }

    public @NotNull Class<? extends S> getSender() {
        final Parameter[] parameters = this.method.getParameters();

        if (parameters.length == 0) {
            throw new IllegalStateException("No sender parameter found.");
        }

        final Class<?> type = parameters[0].getType();

        return (Class<? extends S>) type;
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

    public @NotNull final String getPermission() {
        return this.permission;
    }

    public @NotNull final String getLeaf() {
        return this.leaf;
    }
}