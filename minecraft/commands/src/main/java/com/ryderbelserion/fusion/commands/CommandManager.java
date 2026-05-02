package com.ryderbelserion.fusion.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.commands.api.objects.LeafCommand;
import com.ryderbelserion.fusion.commands.api.objects.TreeCommand;
import com.ryderbelserion.fusion.commands.api.TreeProcessor;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public abstract class CommandManager<S extends Audience> {

    protected final Map<String, TreeCommand> commands = new HashMap<>();

    public void parse(@NotNull final TreeCommand tree) {
        final TreeProcessor<S> root = tree.getProcessor();

        final LiteralArgumentBuilder<S> builder = root.process(tree).getBuilder();

        for (final LeafCommand leaf : root.processTree(tree.getClass().getDeclaredMethods())) {
            builder.then(leaf.execute(tree));
        }

        for (final Object index : tree.getCommands()) {
            root.process(index);
        }

        final String branch = root.getTree();

        this.commands.putIfAbsent(branch, tree);

        init(branch);
    }

    public abstract boolean hasPermission(@NotNull final S context, @NotNull final String permission);

    public abstract void init(@NotNull final String key);
}