package com.ryderbelserion.fusion.example.commands;

import com.mojang.brigadier.Command;
import com.ryderbelserion.fusion.example.commands.types.CommandReload;
import com.ryderbelserion.fusion.velocity.api.commands.objects.AbstractVelocityCommand;
import com.ryderbelserion.fusion.velocity.api.commands.objects.AbstractVelocityContext;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class BaseCommand extends AbstractVelocityCommand {

    private final ProxyServer server;

    public BaseCommand(@NotNull final ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(@NotNull final AbstractVelocityContext context) {
        context.getCommandSender().sendRichMessage("<red>This is the base command!");
    }

    @Override
    public boolean requirement(@NotNull final CommandSource sender) {
        return sender.hasPermission(getPermissions().getFirst());
    }

    @Override
    public @NotNull final List<String> getPermissions() {
        return List.of("fusion.use");
    }

    @Override
    public @NotNull BrigadierCommand build() {
        return new BrigadierCommand(BrigadierCommand.literalArgumentBuilder("fusion")
                .requires(this::requirement).executes(context -> {
                    execute(new AbstractVelocityContext(context));

                    return Command.SINGLE_SUCCESS;
                }).build());
    }

    @Override
    public @NotNull final List<AbstractVelocityCommand> getChildren() {
        return List.of(new CommandReload());
    }
}