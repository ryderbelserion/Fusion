package com.ryderbelserion.fusion.kyori.commands.api.senders.objects;

import org.jetbrains.annotations.NotNull;

public interface SenderMapper<S> {

    @NotNull S map(@NotNull final S sender);

}
