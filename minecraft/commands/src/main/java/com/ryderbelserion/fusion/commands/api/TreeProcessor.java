package com.ryderbelserion.fusion.commands.api;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.commands.CommandManager;
import com.ryderbelserion.fusion.commands.api.objects.types.BranchCommand;
import com.ryderbelserion.fusion.commands.api.objects.types.TreeCommand;
import org.jetbrains.annotations.NotNull;

public class TreeProcessor<S> {

    private LiteralArgumentBuilder<S> builder;
    private String description;

    public @NotNull TreeProcessor processBranch(@NotNull final CommandManager<S> manager, @NotNull final Object object) {
        final BranchCommand<S> command = new BranchCommand<>(object);

        command.build(manager).getBuilder().ifPresent(builder -> this.builder.then(builder));

        return this;
    }

    public @NotNull TreeProcessor process(@NotNull final CommandManager<S> commandManager, @NotNull final Object object) {
        final TreeCommand<S> command = new TreeCommand<>(object);

        command.build(commandManager).getBuilder().ifPresent(builder -> this.builder = builder);

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