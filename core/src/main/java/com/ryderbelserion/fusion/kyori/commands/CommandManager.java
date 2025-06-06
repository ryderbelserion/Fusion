package com.ryderbelserion.fusion.kyori.commands;

import org.jetbrains.annotations.NotNull;

public abstract class CommandManager<S, A> {

    public abstract void enable(@NotNull final A root);

}