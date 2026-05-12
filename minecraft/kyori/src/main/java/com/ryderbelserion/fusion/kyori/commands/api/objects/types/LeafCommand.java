package com.ryderbelserion.fusion.kyori.commands.api.objects.types;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.other.Permission;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.subs.Leaf;
import com.ryderbelserion.fusion.kyori.commands.api.objects.BasicCommand;
import com.ryderbelserion.fusion.kyori.commands.api.objects.meta.types.PermissionMeta;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class LeafCommand<S> extends BasicCommand<S> {

    private final PermissionMeta<S> permissionMeta;
    private final Leaf leaf;

    public LeafCommand(@NotNull final Method method, @NotNull final Object object) {
        super(method, object);

        this.leaf = this.method.getAnnotation(Leaf.class);

        this.permissionMeta = new PermissionMeta<>(this.method.isAnnotationPresent(Permission.class) ? this.method.getAnnotation(Permission.class) : null);
        this.permissionMeta.init();

        this.builder = LiteralArgumentBuilder.literal(this.leaf.value());
    }

    @Override
    public @NotNull final Parameter[] getParameters() {
        return this.parameters;
    }

    @Override
    public @NotNull final LeafCommand<S> build() {
        this.builder.requires(this.permissionMeta::hasPermission).executes(this::invoke);

        return this;
    }

    @Override
    public @NotNull final String getDescription() {
        return this.leaf.desc();
    }
}