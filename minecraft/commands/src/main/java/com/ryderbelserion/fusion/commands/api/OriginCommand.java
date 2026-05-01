package com.ryderbelserion.fusion.commands.api;

import com.ryderbelserion.fusion.commands.processor.RootProcessor;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public abstract class OriginCommand {

    private final RootProcessor processor = new RootProcessor();

    private final List<Object> commands = new ArrayList<>();

    public void addCommand(@NotNull final Object origin) {
        this.commands.add(origin);
    }

    public @NotNull final RootProcessor getProcessor() {
        return this.processor;
    }

    public @NotNull final List<Object> getCommands() {
        return this.commands;
    }
}