package com.ryderbelserion.fusion.kyori.commands.api.objects;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.kyori.commands.CommandManager;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.suggestions.Suggestion;
import com.ryderbelserion.fusion.kyori.commands.api.senders.objects.SenderExtension;
import com.ryderbelserion.fusion.kyori.commands.api.senders.results.ValidationResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BasicCommand<S> {

    protected final FusionKyori fusion = (FusionKyori) FusionProvider.getInstance();

    protected final CommandManager<S, ?> commandManager = this.fusion.getCommandManager();

    protected final SenderExtension<S> extension = this.commandManager.getSenderExtension();

    protected final Parameter[] parameters;
    protected final Method method;
    protected final Object object;

    public BasicCommand(@Nullable final Method method, @NotNull final Object object) {
        this.parameters = method != null ? method.getParameters() : new Parameter[0];
        this.method = method;
        this.object = object;
    }

    protected LiteralArgumentBuilder<S> builder;

    public @NotNull Optional<LiteralArgumentBuilder<S>> getBuilder() {
        return Optional.ofNullable(this.builder);
    }

    public abstract @NotNull Parameter[] getParameters();

    public abstract @NotNull BasicCommand<S> build();

    public abstract @NotNull String getDescription();

    protected int execute(@NotNull final CommandContext<S> context) {
        final Class<? extends S> sender = getSenderType();
        final S source = context.getSource();

        final ValidationResult<String> result = this.extension.validate(sender, context.getSource());

        if (result instanceof ValidationResult.Invalid(String message)) {
            this.extension.sendMessage(source, message);

            return Command.SINGLE_SUCCESS;
        }

        final List<Object> arguments = new ArrayList<>();

        if (this.parameters.length > 0) {
            final Parameter index = this.parameters[0];

            arguments.add(this.extension.map(index.getType(), source));

            for (final Parameter parameter : this.parameters) {
                if (!parameter.isAnnotationPresent(Suggestion.class)) continue;

                final Suggestion suggestion = parameter.getAnnotation(Suggestion.class);

                final String name = suggestion.name();

                final Object arg = context.getArgument(name, parameter.getType());

                arguments.add(arg);
            }
        }

        return invoke(arguments);
    }

    protected int invoke(@NotNull final List<Object> arguments) {
        if (this.method == null || !this.method.trySetAccessible()) return Command.SINGLE_SUCCESS;

        try {
            this.method.invoke(this, arguments.toArray());
        } catch (IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }

        return Command.SINGLE_SUCCESS;
    }

    public @NotNull Class<? extends S> getSenderType() {
        if (this.parameters == null || parameters.length == 0) {
            throw new IllegalStateException("No sender parameter has been found.");
        }

        return this.extension.getSenderType(this.parameters[0].getType());
    }
}