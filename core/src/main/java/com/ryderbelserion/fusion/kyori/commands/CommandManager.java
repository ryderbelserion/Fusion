package com.ryderbelserion.fusion.kyori.commands;

import com.ryderbelserion.fusion.kyori.enums.PermissionMode;
import org.jetbrains.annotations.NotNull;

public abstract class CommandManager<S, A> {

    public abstract void enable(@NotNull final A root);

    public abstract boolean hasPermission(@NotNull final S stack, @NotNull final PermissionMode mode, @NotNull final String[] permissions);

}