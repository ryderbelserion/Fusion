package com.ryderbelserion.fusion.commands;

import com.ryderbelserion.fusion.commands.api.objects.AbstractCommand;
import com.ryderbelserion.fusion.commands.api.TreeProcessor;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public abstract class CommandManager<S> {

    protected final Map<String, AbstractCommand> commands = new HashMap<>();

    public void parse(@NotNull final AbstractCommand tree) {
        final TreeProcessor<S> processor = tree.getProcessor();

        processor.process(this, tree); // process base command

        for (final Object index : tree.getCommands()) {
            processor.processBranch(this, index); // process sub commands added in constructor
        }

        final String branch = processor.getTree();

        this.commands.putIfAbsent(branch, tree);

        init(branch);
    }

    public abstract boolean hasPermission(@NotNull final S context, @NotNull final String permission);

    public abstract void init(@NotNull final String key);
}