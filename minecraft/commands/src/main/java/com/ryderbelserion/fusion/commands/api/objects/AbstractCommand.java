package com.ryderbelserion.fusion.commands.api.objects;

import com.ryderbelserion.fusion.commands.api.TreeProcessor;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand {

    private final TreeProcessor processor = new TreeProcessor();

    private final List<Object> commands = new ArrayList<>();

    public final void addCommand(@NotNull final Object origin) {
        this.commands.add(origin);
    }

    public @NotNull final TreeProcessor getProcessor() {
        return this.processor;
    }

    public @NotNull final List<Object> getCommands() {
        return this.commands;
    }
}