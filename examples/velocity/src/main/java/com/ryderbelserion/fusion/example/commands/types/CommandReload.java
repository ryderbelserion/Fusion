package com.ryderbelserion.fusion.example.commands.types;

import com.mojang.brigadier.Command;
import com.ryderbelserion.fusion.velocity.api.commands.objects.AbstractVelocityCommand;
import com.ryderbelserion.fusion.velocity.api.commands.objects.AbstractVelocityContext;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class CommandReload extends AbstractVelocityCommand {

    @Override
    public void execute(@NotNull final AbstractVelocityContext context) {
        context.getCommandSender().sendRichMessage("<red>This is the reload command!");
    }

    @Override
    public boolean requirement(@NotNull final CommandSource sender) {
        return sender.hasPermission(getPermissions().getFirst());
    }

    @Override
    public @NotNull final List<String> getPermissions() {
        return List.of("fusion.reload");
    }

    @Override
    public @NotNull BrigadierCommand build() {
        return new BrigadierCommand(BrigadierCommand.literalArgumentBuilder("reload")
                .requires(this::requirement).executes(context -> {
                    execute(new AbstractVelocityContext(context));

                    return Command.SINGLE_SUCCESS;
                }).build());
    }
}