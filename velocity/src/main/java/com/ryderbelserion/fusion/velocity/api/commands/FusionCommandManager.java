package com.ryderbelserion.fusion.velocity.api.commands;

import com.ryderbelserion.fusion.common.api.commands.ICommandManager;
import com.ryderbelserion.fusion.velocity.api.commands.objects.VelocityCommand;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class FusionCommandManager extends ICommandManager<CommandSource, VelocityCommand> {

    private final CommandManager commandManager;
    private final Object classObject;
    private final String root;

    public FusionCommandManager(@NotNull final CommandManager commandManager, @NotNull final Object classObject, @NotNull final String root) {
        this.commandManager = commandManager;
        this.classObject = classObject;
        this.root = root;
    }

    @Override
    public void enable(@NotNull final VelocityCommand command, @Nullable final String description, @NotNull final List<String> aliases) {
        final CommandMeta commandMeta = this.commandManager.metaBuilder(this.root)
                .aliases(String.valueOf(aliases))
                .plugin(this.classObject)
                .build();

        final BrigadierCommand brigadierCommand = command.build();

        command.getChildren().forEach(child -> brigadierCommand.getNode().addChild(child.build().getNode()));

        this.commandManager.register(commandMeta, brigadierCommand);
    }

    @Override
    public void disable() {
        this.commandManager.unregister(this.root);
    }
}