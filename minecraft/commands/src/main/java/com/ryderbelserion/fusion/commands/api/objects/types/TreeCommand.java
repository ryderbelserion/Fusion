package com.ryderbelserion.fusion.commands.api.objects.types;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.commands.CommandManager;
import com.ryderbelserion.fusion.commands.api.annotations.Flower;
import com.ryderbelserion.fusion.commands.api.annotations.Tree;
import com.ryderbelserion.fusion.commands.api.annotations.other.Permission;
import com.ryderbelserion.fusion.commands.api.objects.RootCommand;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class TreeCommand<S> extends RootCommand<S, Method> {

    private LiteralArgumentBuilder<S> builder;

    private final boolean isTreePresent;
    private final Class<?> origin;
    private final Object object;

    private final Tree tree;

    private final boolean isPermissionPresent;
    private final String permission;

    public TreeCommand(@NotNull final Object object) {
        this.object = object;
        this.origin = this.object.getClass();

        this.isTreePresent = this.origin.isAnnotationPresent(Tree.class);
        this.tree = this.isTreePresent ? this.origin.getAnnotation(Tree.class) : null;

        this.isPermissionPresent = this.origin.isAnnotationPresent(Permission.class);
        this.permission = this.isPermissionPresent ? this.origin.getAnnotation(Permission.class).permission() : "";
    }

    @Override
    public @NotNull final Optional<LiteralArgumentBuilder<S>> getBuilder() {
        return Optional.ofNullable(this.builder);
    }

    @Override
    public @NotNull final TreeCommand<S> build(@NotNull final CommandManager manager) {
        if (!this.isTreePresent) return this;

        this.builder = LiteralArgumentBuilder.literal(this.tree.value());

        if (this.isPermissionPresent && !this.permission.isBlank()) {
            this.builder.requires(context -> manager.hasPermission(context, this.permission));
        }

        final Method[] keys = this.origin.getDeclaredMethods();

        final List<Method> methods = filter(keys, insect -> insect.isAnnotationPresent(Flower.class));

        if (!methods.isEmpty()) {
            this.builder.executes(_ -> invoke(methods.getFirst(), this.object));
        }

        final List<LeafCommand<S>> leaves = process(keys, this.object);

        for (final LeafCommand<S> leaf : leaves) {
            leaf.getBuilder().ifPresent(builder -> this.builder.then(builder));
        }

        return this;
    }

    @Override
    public @NotNull final String getDescription() {
        return this.tree.desc();
    }
}