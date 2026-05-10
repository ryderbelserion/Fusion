package com.ryderbelserion.fusion.kyori.commands;

import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.kyori.commands.api.objects.AbstractCommand;
import com.ryderbelserion.fusion.kyori.commands.api.TreeProcessor;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class CommandManager<S> {

    protected final FusionKyori fusion = (FusionKyori) FusionProvider.getInstance();

    protected final Map<String, AbstractCommand> commands = new HashMap<>();

    protected final Map<Key, String> messages = new HashMap<>();

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

    public void addMessage(@NotNull final Key key, @NotNull final String message) {
        this.messages.put(key, message);
    }

    public Optional<String> getMessage(@NotNull final Key key) {
        return Optional.ofNullable(this.messages.get(key));
    }

    public abstract void post(@NotNull final String key);

    public abstract void init();
}