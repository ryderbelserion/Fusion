package com.ryderbelserion.fusion.kyori.commands.api.objects.types.methods;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.Flower;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.other.Permission;
import com.ryderbelserion.fusion.kyori.commands.api.objects.BasicCommand;
import com.ryderbelserion.fusion.kyori.commands.api.objects.meta.types.PermissionMeta;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class FlowerCommand<S> extends BasicCommand<S> {

    private final PermissionMeta<S> permissionMeta;
    private final Flower flower;

    public FlowerCommand(
            @NotNull final LiteralArgumentBuilder<S> builder,
            @NotNull final Method method,
            @NotNull final Object object
    ) {
        super(method, object);

        this.builder = builder;

        this.flower = this.method.getAnnotation(Flower.class);

        this.permissionMeta = new PermissionMeta<>(this.method.isAnnotationPresent(Permission.class) ? this.method.getAnnotation(Permission.class) : null);
        this.permissionMeta.init();
    }

    @Override
    public @NotNull final Parameter[] getParameters() {
        return this.parameters;
    }

    @Override
    public @NotNull final FlowerCommand<S> build() {
        this.builder.requires(this.permissionMeta::hasPermission).executes(this::invoke);

        return this;
    }

    @Override
    public @NotNull final String getDescription() {
        return this.flower.desc();
    }
}