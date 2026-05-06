package com.ryderbelserion.fusion.commands.api.objects.types;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.commands.CommandManager;
import com.ryderbelserion.fusion.commands.api.annotations.other.Permission;
import com.ryderbelserion.fusion.commands.api.annotations.subs.Leaf;
import com.ryderbelserion.fusion.commands.api.objects.BasicCommand;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

public class LeafCommand<S> extends BasicCommand<S> {

    private LiteralArgumentBuilder<S> builder;

    private final Parameter[] parameters;

    private final boolean isLeafPresent;
    private final Method method;
    private final Object object;
    private final Leaf leaf;

    private final boolean isPermissionPresent;
    private final String permission;

    public LeafCommand(@NotNull final Method method, @NotNull final Object object) {
        this.parameters = method.getParameters();
        this.method = method;
        this.object = object;

        this.isLeafPresent = this.method.isAnnotationPresent(Leaf.class);
        this.leaf = this.isLeafPresent ? this.method.getAnnotation(Leaf.class) : null;

        this.isPermissionPresent = this.method.isAnnotationPresent(Permission.class);
        this.permission = this.isPermissionPresent ? this.method.getAnnotation(Permission.class).permission() : "";
    }

    public @NotNull Class<? extends S> getSender() {
        final Parameter[] parameters = this.parameters;

        if (parameters.length == 0) {
            throw new IllegalStateException("No sender parameter found.");
        }

        final Class<?> type = parameters[0].getType();

        return (Class<? extends S>) type;
    }

    @Override
    public @NotNull final Optional<LiteralArgumentBuilder<S>> getBuilder() {
        return Optional.ofNullable(this.builder);
    }

    @Override
    public @NotNull final LeafCommand<S> build(@NotNull final CommandManager manager) {
        if (!this.isLeafPresent) return this;

        this.builder = LiteralArgumentBuilder.literal(this.leaf.value());

        if (this.isPermissionPresent && !this.permission.isBlank()) {
            this.builder.requires(context -> manager.hasPermission(context, this.permission));
        }

        this.builder.executes(_ -> invoke(this.method, this.object));

        return this;
    }

    @Override
    public @NotNull final String getDescription() {
        return this.leaf.desc();
    }
}