package com.ryderbelserion.fusion.kyori.commands.api.objects.meta.types;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public class ArgumentMeta<S> {

    private RequiredArgumentBuilder<S, ?> argument;

    public final void then(@NotNull final RequiredArgumentBuilder<S, ?> argument) {
        if (this.argument == null) {
            this.argument = argument;

            return;
        }

        this.argument.then(argument);
    }

    public @NotNull final Optional<RequiredArgumentBuilder<S, ?>> getArgument() {
        return Optional.ofNullable(this.argument);
    }

    public @NotNull final ArgumentType<?> map(@NotNull final Class<?> klass) {
        final String type = klass.getSimpleName();

        ArgumentType<?> argumentType = StringArgumentType.string();

        switch (type) {
            case "boolean" -> argumentType = BoolArgumentType.bool();
            case "double" -> argumentType = DoubleArgumentType.doubleArg();
            case "int" -> argumentType = IntegerArgumentType.integer();
        }

        return argumentType;
    }
}