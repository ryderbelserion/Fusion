package com.ryderbelserion.fusion.core.api.objects;

import org.jetbrains.annotations.NotNull;

public abstract class StringKey {

    private final String key;

    public StringKey(@NotNull final String key) {
        this.key = key;
    }

    public @NotNull String toString() {
        return "StringKey{key='" + this.key + "'}";
    }
}