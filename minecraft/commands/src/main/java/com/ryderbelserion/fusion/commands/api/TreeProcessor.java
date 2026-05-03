package com.ryderbelserion.fusion.commands.api;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.commands.CommandManager;
import com.ryderbelserion.fusion.commands.api.annotations.subs.Leaf;
import com.ryderbelserion.fusion.commands.api.objects.types.BranchCommand;
import com.ryderbelserion.fusion.commands.api.objects.types.LeafCommand;
import com.ryderbelserion.fusion.commands.api.objects.types.TreeCommand;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TreeProcessor<S> {

    private LiteralArgumentBuilder<S> builder;
    private String description;

    public @NotNull List<LeafCommand<S>> processTree(@NotNull final CommandManager<S> commandManager, @NotNull final Method[] methods) {
        return Arrays.stream(methods)
                .filter(insect -> insect.isAnnotationPresent(Leaf.class))
                .sorted(Comparator.comparingInt(insect -> insect.getAnnotation(Leaf.class).weight()))
                .map(map -> new LeafCommand<>(commandManager, map, map.getAnnotation(Leaf.class)))
                .toList();
    }

    public @NotNull TreeProcessor processBranch(@NotNull final CommandManager<S> manager, @NotNull final Object object) {
        final BranchCommand<S> command = new BranchCommand<>(object);

        command.build(manager).getBuilder().ifPresent(builder -> this.builder.then(builder));

        return this;
    }

    public @NotNull TreeProcessor process(@NotNull final CommandManager<S> commandManager, @NotNull final Object object) {
        final TreeCommand<S> command = new TreeCommand<>(object);

        command.build(commandManager).getBuilder().ifPresent(builder -> this.builder.then(builder));

        this.description = command.getDescription();

        return this;
    }

    public @NotNull final LiteralArgumentBuilder<S> getBuilder() {
        return this.builder;
    }

    public @NotNull final String getDescription() {
        return this.description;
    }

    public @NotNull final String getTree() {
        return this.builder.getLiteral();
    }
}