package com.ryderbelserion.fusion.commands.api.objects.types;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.commands.CommandManager;
import com.ryderbelserion.fusion.commands.api.annotations.Flower;
import com.ryderbelserion.fusion.commands.api.objects.BasicCommand;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

public class FlowerCommand<S> extends BasicCommand<S> {

    private final LiteralArgumentBuilder<S> builder;
    private final Parameter[] parameters;

    private final Method method;
    private final Object object;

    private final Flower flower;

    public FlowerCommand(
            @NotNull final LiteralArgumentBuilder<S> builder,
            @NotNull final Method method,
            @NotNull final Object object
    ) {
        this.parameters = method.getParameters();
        this.builder = builder;
        this.method = method;
        this.object = object;

        this.flower = this.method.getAnnotation(Flower.class);
    }

    @Override
    public @NotNull final Optional<LiteralArgumentBuilder<S>> getBuilder() {
        return Optional.ofNullable(this.builder);
    }

    @Override
    public @NotNull final FlowerCommand<S> build(@NotNull final CommandManager manager) {
        this.builder.executes(_ -> invoke(this.method, this.object));

        return this;
    }

    @Override
    public @NotNull final String getDescription() {
        return this.flower.desc();
    }
}