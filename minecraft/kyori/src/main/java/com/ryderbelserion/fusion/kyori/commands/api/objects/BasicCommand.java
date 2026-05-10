package com.ryderbelserion.fusion.kyori.commands.api.objects;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.kyori.commands.CommandManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.Set;

public abstract class BasicCommand<S> {

    protected final FusionKyori fusion = (FusionKyori) FusionProvider.getInstance();

    protected final CommandManager<S, ?> commandManager = this.fusion.getCommandManager();

    protected final Method method;
    protected final Object object;

    public BasicCommand(@Nullable final Method method, @NotNull final Object object) {
        this.method = method;
        this.object = object;
    }

    public abstract @NotNull Optional<LiteralArgumentBuilder<S>> getBuilder();

    public abstract @NotNull Parameter[] getParameters();

    public abstract @NotNull String getDescription();

    public abstract @NotNull BasicCommand build();

    protected int invoke(
            @NotNull final CommandContext<S> context
    ) {
        if (this.method == null) return Command.SINGLE_SUCCESS;

        if (!this.method.trySetAccessible()) return Command.SINGLE_SUCCESS;

        try {
            this.method.invoke(this.object);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }

        return Command.SINGLE_SUCCESS;
    }

    public @NotNull Class<? extends S> getSender() {
        final Parameter[] parameters = getParameters();

        if (parameters.length == 0) {
            throw new IllegalStateException("No sender parameter found.");
        }

        final Class<?> type = parameters[0].getType();
        final Set<Class<?>> senders = this.commandManager.getSenderExtension().getSenders();

        if (!senders.contains(type)) {
            throw new IllegalStateException("%s is not a valid sender.".formatted(type.getSimpleName()));
        }

        return (Class<? extends S>) type;
    }
}