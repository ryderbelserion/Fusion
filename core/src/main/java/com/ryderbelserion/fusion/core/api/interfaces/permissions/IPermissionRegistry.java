package com.ryderbelserion.fusion.core.api.interfaces.permissions;

import org.jetbrains.annotations.NotNull;
import java.util.List;

public interface IPermissionRegistry<P> {

    void addPermissions(@NotNull final String namespace, @NotNull final List<P> permissions);

    void addPermission(@NotNull final String namespace, @NotNull final P permission);

    void start();

}