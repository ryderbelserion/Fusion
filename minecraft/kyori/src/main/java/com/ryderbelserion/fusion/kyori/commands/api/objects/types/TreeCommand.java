package com.ryderbelserion.fusion.kyori.commands.api.objects.types;

import com.ryderbelserion.fusion.kyori.commands.api.annotations.Tree;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.other.Permission;
import com.ryderbelserion.fusion.kyori.commands.api.objects.RootCommand;
import com.ryderbelserion.fusion.kyori.commands.api.objects.meta.types.PermissionMeta;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class TreeCommand<S> extends RootCommand<S, Method> {

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
    public @NotNull final Parameter[] getParameters() {
        return new Parameter[0];
    }

    @Override
    public @NotNull final TreeCommand<S> build() {
        if (!this.isTreePresent) return this;

        this.builder = getBrigadier(this.tree.value(), this.permissionMeta);

        return this;
    }

    @Override
    public @NotNull final String getDescription() {
        return this.tree.desc();
    }
}