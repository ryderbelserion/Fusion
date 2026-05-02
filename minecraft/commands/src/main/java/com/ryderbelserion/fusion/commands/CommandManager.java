package com.ryderbelserion.fusion.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.commands.annotations.Leaf;
import com.ryderbelserion.fusion.commands.api.TreeCommand;
import com.ryderbelserion.fusion.commands.processor.TreeProcessor;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public abstract class CommandManager<S> {

    protected final Map<String, TreeCommand> commands = new HashMap<>();

    public void parse(@NotNull final TreeCommand tree) {
        final TreeProcessor<S> root = tree.getProcessor();

        final LiteralArgumentBuilder<S> builder = root.process(tree).getBuilder();

        for (final Leaf leaf : root.processTree(tree.getClass().getDeclaredMethods())) {
            builder.then(LiteralArgumentBuilder.literal(leaf.value()));
        }

        for (final Object index : tree.getCommands()) {
            root.process(index);
        }

        final String branch = root.getTree();

        this.commands.putIfAbsent(branch, tree);

        init(branch);
    }

    public abstract void init(@NotNull final String key);
}