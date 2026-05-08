package com.ryderbelserion.fusion.commands.api.args.objects;

import org.jetbrains.annotations.NotNull;

public class SenderData<T> {

    // 0 = failure, 1 = success
    private final int status;

    private T sender = null;

    public SenderData(final int status) {
        this.status = status;
    }

    public void setSender(@NotNull final T sender) {
        this.sender = sender;
    }

    public @NotNull final T getSender() {
        return this.sender;
    }

    public final int getStatus() {
        return this.status;
    }
}