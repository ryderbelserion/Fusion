package com.ryderbelserion.fusion.kyori.commands.api.objects.types;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.other.Permission;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.subs.Leaf;
import com.ryderbelserion.fusion.kyori.commands.api.objects.BasicCommand;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

public class LeafCommand<S> extends BasicCommand<S> {

    private LiteralArgumentBuilder<S> builder;

    private final boolean isLeafPresent;
    private final Leaf leaf;

    private final boolean isPermissionPresent;
    private final String permission;

    public LeafCommand(@NotNull final Method method, @NotNull final Object object) {
        super(method, object);

        this.isLeafPresent = this.method.isAnnotationPresent(Leaf.class);
        this.leaf = this.isLeafPresent ? this.method.getAnnotation(Leaf.class) : null;

        this.isPermissionPresent = this.method.isAnnotationPresent(Permission.class);
        this.permission = this.isPermissionPresent ? this.method.getAnnotation(Permission.class).permission() : "";
    }

    @Override
    public @NotNull final Optional<LiteralArgumentBuilder<S>> getBuilder() {
        return Optional.ofNullable(this.builder);
    }

    @Override
    public @NotNull final Parameter[] getParameters() {
        return this.parameters;
    }

    @Override
    public @NotNull final LeafCommand<S> build() {
        if (!this.isLeafPresent) return this;

        this.builder = LiteralArgumentBuilder.literal(this.leaf.value());

        if (this.isPermissionPresent && !this.permission.isBlank()) {
            this.builder.requires(context -> this.fusion.hasPermission(context, this.permission));
        }

        this.builder.executes(this::invoke);

        return this;
    }

    @Override
    public @NotNull final String getDescription() {
        return this.leaf.desc();
    }
}