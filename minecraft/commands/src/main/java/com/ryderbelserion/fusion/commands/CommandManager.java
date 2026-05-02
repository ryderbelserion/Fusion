package com.ryderbelserion.fusion.commands;

import com.ryderbelserion.fusion.commands.api.TreeCommand;
import com.ryderbelserion.fusion.commands.processor.TreeProcessor;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public abstract class CommandManager<S> {

    protected final Map<String, TreeCommand> commands = new HashMap<>();

    public void parse(@NotNull final TreeCommand tree) {
        final TreeProcessor<S> root = tree.getProcessor();

        root.process(tree).processTree(root.getClass().getDeclaredMethods());

        for (final Object index : tree.getCommands()) {
            root.process(index);
        }

        final String branch = root.getTree();

        this.commands.putIfAbsent(branch, tree);

        init(branch);
    }

    public abstract void init(@NotNull final String key);
}