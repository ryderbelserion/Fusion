package com.ryderbelserion.fusion.commands.api.objects;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.commands.CommandManager;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public abstract class BasicCommand<S> {

    public abstract @NotNull BasicCommand build(@NotNull final CommandManager manager);

    public abstract @NotNull Optional<LiteralArgumentBuilder<S>> getBuilder();

    public abstract @NotNull String getDescription();

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