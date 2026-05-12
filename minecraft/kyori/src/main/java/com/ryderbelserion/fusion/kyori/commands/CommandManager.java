package com.ryderbelserion.fusion.kyori.commands;

import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.kyori.commands.api.objects.api.AbstractCommand;
import com.ryderbelserion.fusion.kyori.commands.api.TreeProcessor;
import com.ryderbelserion.fusion.kyori.commands.api.senders.objects.SenderExtension;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public abstract class CommandManager<S, E extends SenderExtension<S>> {

    protected final FusionKyori fusion = (FusionKyori) FusionProvider.getInstance();

    protected final Map<String, AbstractCommand> commands = new HashMap<>();

    public final void parse(@NotNull final AbstractCommand tree) {
        final TreeProcessor<S> processor = tree.getProcessor();

        processor.process(tree); // process base command

        for (final Object index : tree.getCommands()) {
            processor.processBranch(index); // process sub commands added in constructor
        }

        final String branch = processor.getTree();

        this.commands.putIfAbsent(branch, tree);

        post(branch);
    }

    public abstract void post(@NotNull final String key);

    public abstract E getSenderExtension();

    public abstract void init();
}