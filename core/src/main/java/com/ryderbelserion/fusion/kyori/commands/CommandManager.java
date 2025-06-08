package com.ryderbelserion.fusion.kyori.commands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class CommandManager<S, A> {

    public abstract void enable(@NotNull final A root, @Nullable final String description, @NotNull final List<String> aliases);

    public abstract void disable();

}