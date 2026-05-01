package com.ryderbelserion.fusion.commands.api.objects;

import org.jetbrains.annotations.NotNull;

public class Argument {

    private final String argument;
    private final int weight;

    public Argument(@NotNull final String argument, final int weight) {
        this.argument = argument;
        this.weight = weight;
    }

    public @NotNull final String getArgument() {
        return this.argument;
    }

    public final int getWeight() {
        return this.weight;
    }
}