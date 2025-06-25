package com.ryderbelserion.fusion.common.api.commands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public abstract class ICommandManager<S, A> {

    public abstract void enable(@NotNull final A root, @Nullable final String description, @NotNull final List<String> aliases);

    public abstract void disable();

}