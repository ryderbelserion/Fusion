package com.ryderbelserion.fusion.commands;

import com.ryderbelserion.fusion.commands.api.args.ISender;
import com.ryderbelserion.fusion.commands.api.objects.AbstractCommand;
import com.ryderbelserion.fusion.commands.api.TreeProcessor;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class CommandManager<S> {

    protected final Map<String, AbstractCommand> commands = new HashMap<>();

    protected final Set<ISender<?, S>> senders = new HashSet<>();

    public final void parse(@NotNull final AbstractCommand tree) {
        final TreeProcessor<S> processor = tree.getProcessor();

        processor.process(this, tree); // process base command

        for (final Object index : tree.getCommands()) {
            processor.processBranch(this, index); // process sub commands added in constructor
        }

        final String branch = processor.getTree();

        this.commands.putIfAbsent(branch, tree);

        init(branch);
    }

    public final void addSender(@NotNull final ISender<?, S> sender) {
        this.senders.add(sender);
    }

    public abstract boolean hasPermission(@NotNull final S context, @NotNull final String permission);

    public abstract void init(@NotNull final String key);
}