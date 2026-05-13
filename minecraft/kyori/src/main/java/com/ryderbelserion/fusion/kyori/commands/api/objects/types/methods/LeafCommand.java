package com.ryderbelserion.fusion.kyori.commands.api.objects.types.methods;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.other.Permission;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.subs.Leaf;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.suggestions.Suggestion;
import com.ryderbelserion.fusion.kyori.commands.api.objects.BasicCommand;
import com.ryderbelserion.fusion.kyori.commands.api.objects.meta.types.ArgumentMeta;
import com.ryderbelserion.fusion.kyori.commands.api.objects.meta.types.PermissionMeta;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.Random;

public class LeafCommand<S> extends BasicCommand<S> {

    private final PermissionMeta<S> permissionMeta;
    private final ArgumentMeta<S> argumentMeta;
    private final Leaf leaf;

    public LeafCommand(@NotNull final Method method, @NotNull final Object object) {
        super(method, object);

        this.leaf = this.method.getAnnotation(Leaf.class);

        this.permissionMeta = new PermissionMeta<>(this.method.isAnnotationPresent(Permission.class) ? this.method.getAnnotation(Permission.class) : null);
        this.permissionMeta.init();

        this.argumentMeta = new ArgumentMeta<>();

        this.builder = LiteralArgumentBuilder.literal(this.leaf.value());
    }

    @Override
    public @NotNull final Parameter[] getParameters() {
        return this.parameters;
    }

    @Override
    public @NotNull final LeafCommand<S> build() {
        this.builder.requires(this.permissionMeta::hasPermission);

        for (final Parameter parameter : this.parameters) {
            if (!parameter.isAnnotationPresent(Suggestion.class)) continue;

            final Suggestion suggestion = parameter.getAnnotation(Suggestion.class);

            final ArgumentType<?> argumentType = argumentMeta.map(suggestion.type());

            final String name = suggestion.name();

            final RequiredArgumentBuilder<S, ?> arg = RequiredArgumentBuilder.argument(name, argumentType);

            arg.suggests((_, builder) -> builder.suggest(new Random().nextInt(10)).buildFuture());

            this.argumentMeta.then(arg);
        }

        final Optional<RequiredArgumentBuilder<S, ?>> argument = argumentMeta.getArgument();

        argument.ifPresentOrElse(context -> this.builder.then(context).executes(this::execute), () -> this.builder.executes(this::execute));

        return this;
    }

    @Override
    public @NotNull final String getDescription() {
        return this.leaf.desc();
    }
}