package com.ryderbelserion.fusion.core.api.commands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

/**
 * The command manager class to extend.
 *
 * @param <S> the platform's CommandSourceStack, or otherwise the sender.
 * @param <A> the platform's version of ICommandContext.
 */
//todo() what the fuck was I doing with S?
public abstract class ICommandManager<S, A> {

    public abstract void enable(@NotNull final A root, @Nullable final String description, @NotNull final List<String> aliases);

    public abstract void disable();

}