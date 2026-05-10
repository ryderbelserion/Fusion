package com.ryderbelserion.fusion.kyori.commands.api.objects;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.ryderbelserion.fusion.kyori.commands.CommandManager;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

public abstract class BasicCommand<S> {

    public abstract @NotNull BasicCommand build(@NotNull final CommandManager manager);

    public abstract @NotNull Optional<LiteralArgumentBuilder<S>> getBuilder();

    public abstract @NotNull Parameter[] getParameters();

    public abstract @NotNull String getDescription();

    protected int invoke(@NotNull final CommandContext<S> context, @NotNull final Method method, @NotNull final Object object) {
        if (!method.trySetAccessible()) return Command.SINGLE_SUCCESS;

        try {
            method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }

        return Command.SINGLE_SUCCESS;
    }

    public @NotNull Class<? extends D> getSender() {
        final Parameter[] parameters = getParameters();

        if (parameters.length == 0) {
            throw new IllegalStateException("No sender parameter found.");
        }

        final Class<?> type = parameters[0].getType();

        return (Class<? extends D>) type;
    }
}