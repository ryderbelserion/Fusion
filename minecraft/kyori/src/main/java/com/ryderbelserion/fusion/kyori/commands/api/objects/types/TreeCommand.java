package com.ryderbelserion.fusion.kyori.commands.api.objects.types;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.Tree;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.other.Permission;
import com.ryderbelserion.fusion.kyori.commands.api.objects.BasicCommand;
import com.ryderbelserion.fusion.kyori.commands.api.objects.RootCommand;
import com.ryderbelserion.fusion.kyori.commands.api.objects.meta.types.PermissionMeta;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;

public class TreeCommand<S> extends RootCommand<S, Method> {

    private LiteralArgumentBuilder<S> builder;

    private final PermissionMeta<S> permissionMeta;
    private final boolean isTreePresent;
    private final Class<?> parent;

    private final Tree tree;

    public TreeCommand(@NotNull final Object object) {
        super(null, object);

        this.parent = this.object.getClass();

        this.isTreePresent = this.parent.isAnnotationPresent(Tree.class);
        this.tree = this.isTreePresent ? this.parent.getAnnotation(Tree.class) : null;

        this.permissionMeta = new PermissionMeta<>(this.parent.isAnnotationPresent(Permission.class) ? this.parent.getAnnotation(Permission.class) : null);
        this.permissionMeta.init();
    }

    @Override
    public @NotNull final Optional<LiteralArgumentBuilder<S>> getBuilder() {
        return Optional.ofNullable(this.builder);
    }

    @Override
    public @NotNull final Parameter[] getParameters() {
        return new Parameter[0];
    }

    @Override
    public @NotNull final TreeCommand<S> build() {
        if (!this.isTreePresent) return this;

        this.builder = LiteralArgumentBuilder.literal(this.tree.value());

        this.builder.requires(this.permissionMeta::hasPermission);

        final Method[] keys = this.parent.getDeclaredMethods();

        flower(this.builder, keys, this.object).ifPresent(BasicCommand::build);

        final List<LeafCommand<S>> leaves = process(keys, this.object);

        for (final LeafCommand<S> leaf : leaves) {
            leaf.build().getBuilder().ifPresent(builder -> this.builder.then(builder));
        }

        final Class<?>[] values = this.parent.getDeclaredClasses();

        for (final Class<?> origin : values) {
            final BranchCommand<S> branch = new BranchCommand(origin);

            branch.build().getBuilder().ifPresent(builder -> this.builder.then(builder));
        }

        return this;
    }

    @Override
    public @NotNull final String getDescription() {
        return this.tree.desc();
    }
}