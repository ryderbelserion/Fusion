package com.ryderbelserion.fusion.commands.api.objects.types;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.commands.CommandManager;
import com.ryderbelserion.fusion.commands.api.annotations.other.Permission;
import com.ryderbelserion.fusion.commands.api.annotations.subs.Branch;
import com.ryderbelserion.fusion.commands.api.objects.RootCommand;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class BranchCommand<S> extends RootCommand<S, Method> {

    private LiteralArgumentBuilder<S> builder;

    private final boolean isBranchPresent;
    private final Class<?> origin;
    private final Object object;

    private final Branch branch;

    private final boolean isPermissionPresent;
    private final String permission;

    public BranchCommand(@NotNull final Object object) {
        this.object = object;
        this.origin = this.object.getClass();

        this.isBranchPresent = this.origin.isAnnotationPresent(Branch.class);
        this.branch = this.isBranchPresent ? this.origin.getAnnotation(Branch.class) : null;

        this.isPermissionPresent = this.origin.isAnnotationPresent(Permission.class);
        this.permission = this.isPermissionPresent ? this.origin.getAnnotation(Permission.class).permission() : "";
    }

    @Override
    public @NotNull final Optional<LiteralArgumentBuilder<S>> getBuilder() {
        return Optional.ofNullable(this.builder);
    }

    @Override
    public @NotNull final BranchCommand<S> build(@NotNull final CommandManager manager) {
        if (!this.isBranchPresent) return this;

        this.builder = LiteralArgumentBuilder.literal(this.branch.value());

        if (this.isPermissionPresent && !this.permission.isBlank()) {
            this.builder.requires(context -> manager.hasPermission(context, this.permission));
        }

        final Method[] keys = this.origin.getDeclaredMethods();

        flower(this.builder, keys, this.object).ifPresent(flower -> flower.build(manager));

        final List<LeafCommand<S>> leaves = process(keys, this.object);

        for (final LeafCommand<S> leaf : leaves) {
            leaf.getBuilder().ifPresent(builder -> this.builder.then(builder));
        }

        return this;
    }

    @Override
    public @NotNull final String getDescription() {
        return this.branch.desc();
    }
}