package com.ryderbelserion.fusion.commands.api.objects;


import com.ryderbelserion.fusion.commands.api.annotations.subs.Leaf;
import com.ryderbelserion.fusion.commands.api.objects.types.LeafCommand;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public abstract class RootCommand<S, M> extends BasicCommand<S> { // only for TreeCommand, BranchCommand

    protected @NotNull final List<LeafCommand<S>> process(@NotNull final Method[] methods, @NotNull final Object object) {
        return Arrays.stream(methods)
                .filter(insect -> insect.isAnnotationPresent(Leaf.class))
                .filter(insect -> {
                    final int modifiers = insect.getModifiers();

                    return !Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers);
                })
                //.sorted(Comparator.comparingInt(insect -> insect.getAnnotation(Leaf.class).weight()))
                .map(method -> new LeafCommand<S>(method, object))
                .toList();
    }

    protected @NotNull final List<M> filter(@NotNull final Method[] methods, @NotNull final Predicate<? super M> predicate) {
        return Arrays.stream(methods).map(method -> (M) method).filter(predicate).toList();
    }
}