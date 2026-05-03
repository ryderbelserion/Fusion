package com.ryderbelserion.fusion.commands.api.objects;

import com.mojang.brigadier.Command;
import com.ryderbelserion.fusion.commands.CommandManager;
import com.ryderbelserion.fusion.commands.api.annotations.subs.Leaf;
import com.ryderbelserion.fusion.commands.api.objects.types.LeafCommand;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public abstract class RootCommand<S, M> {

    public abstract @NotNull RootCommand build(@NotNull final CommandManager manager);

    public abstract @NotNull String getDescription();

    protected @NotNull final List<LeafCommand<S>> process(@NotNull final CommandManager<S> commandManager, @NotNull final Method[] methods) {
        return Arrays.stream(methods)
                .filter(insect -> insect.isAnnotationPresent(Leaf.class))
                .filter(insect -> {
                    final int modifiers = insect.getModifiers();

                    return !Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers);
                })
                //.sorted(Comparator.comparingInt(insect -> insect.getAnnotation(Leaf.class).weight()))
                .map(method -> new LeafCommand<>(commandManager, method, method.getAnnotation(Leaf.class)))
                .toList();
    }

    protected @NotNull final List<M> filter(@NotNull final Method[] methods, @NotNull final Predicate<? super M> predicate) {
        return Arrays.stream(methods).map(method -> (M) method).filter(predicate).toList();
    }

    protected int invoke(@NotNull final Method method, @NotNull final Object object) {
        if (!method.trySetAccessible()) return Command.SINGLE_SUCCESS;

        try {
            method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }

        return Command.SINGLE_SUCCESS;
    }
}