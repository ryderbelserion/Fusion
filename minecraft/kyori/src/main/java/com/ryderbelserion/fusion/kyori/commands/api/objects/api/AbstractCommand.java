package com.ryderbelserion.fusion.kyori.commands.api.objects.api;

import com.ryderbelserion.fusion.kyori.commands.api.TreeProcessor;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand<S> {

    private final TreeProcessor<S> processor = new TreeProcessor();

    private final List<Object> commands = new ArrayList<>();

    public final void addCommand(@NotNull final Object origin) {
        this.commands.add(origin);
    }

    public @NotNull final TreeProcessor<S> getProcessor() {
        return this.processor;
    }

    public @NotNull final List<Object> getCommands() {
        return this.commands;
    }
}