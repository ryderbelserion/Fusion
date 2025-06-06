package com.ryderbelserion.fusion.kyori.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.kyori.enums.PermissionMode;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public abstract class CommandManager<S> {

    public abstract void enable(@NotNull final LiteralCommandNode<S> root, @NotNull final List<LiteralCommandNode<S>> children);

    public abstract boolean hasPermission(@NotNull final S stack, @NotNull final PermissionMode mode, @NotNull final String[] permissions);

}