package com.ryderbelserion.fusion.kyori.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public abstract class CommandManager<S> {

    public abstract void enable(@NotNull final LiteralCommandNode<S> root, @NotNull final List<LiteralCommandNode<S>> children);

}