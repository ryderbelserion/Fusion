package com.ryderbelserion.fusion.commands;

import com.ryderbelserion.fusion.commands.api.OriginCommand;
import com.ryderbelserion.fusion.commands.processor.RootProcessor;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public abstract class CommandManager<S> {

    protected final Map<String, OriginCommand> commands = new HashMap<>();

    public void parse(@NotNull final OriginCommand origin) {
        final RootProcessor<S> root = origin.getProcessor();

        root.process(origin).processTree(root.getClass().getDeclaredMethods());

        for (final Object index : origin.getCommands()) {
            root.process(index);
        }

        final String key = root.getOrigin();

        this.commands.putIfAbsent(key, origin);

        init(key);
    }

    public abstract void init(@NotNull final String key);
}