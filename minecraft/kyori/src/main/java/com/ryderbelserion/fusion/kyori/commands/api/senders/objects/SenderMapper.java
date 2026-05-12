package com.ryderbelserion.fusion.kyori.commands.api.senders.objects;

import org.jetbrains.annotations.NotNull;

public interface SenderMapper<S> {

    @NotNull Object map(@NotNull final Class<?> type, @NotNull final S sender);

}