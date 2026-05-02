package com.ryderbelserion.fusion.commands.api.objects;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.commands.api.annotations.other.Permission;
import com.ryderbelserion.fusion.commands.api.annotations.subs.Leaf;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LeafCommand<S extends Audience> {

    private final String permission;
    private final Method method;
    private final String leaf;

    public LeafCommand(@NotNull final Method method, @NotNull final Leaf leaf) {
        this.method = method;

        this.permission = this.method.isAnnotationPresent(Permission.class) ? this.method.getAnnotation(Permission.class).permission() : "";

        this.leaf = leaf.value();
    }

    public LiteralArgumentBuilder<S> execute(@NotNull final Object object) {
        final LiteralArgumentBuilder<S> builder = LiteralArgumentBuilder.literal(this.leaf);

        if (!this.permission.isBlank()) {
            //builder.requires(requirement -> {
                //return requirement.
            //});
        }

        builder.executes(_ -> invoke(this.method, object));

        return builder;
    }

    public int invoke(@NotNull final Method method, @NotNull final Object object) {
        if (!method.trySetAccessible()) return Command.SINGLE_SUCCESS;

        try {
            method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }

        return Command.SINGLE_SUCCESS;
    }

    public @NotNull final String getPermission() {
        return this.permission;
    }

    public @NotNull final Method getMethod() {
        return this.method;
    }

    public @NotNull final String getLeaf() {
        return this.leaf;
    }
}