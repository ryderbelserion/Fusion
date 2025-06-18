package com.ryderbelserion.fusion.example.commands.types;

import com.mojang.brigadier.Command;
import com.ryderbelserion.fusion.kyori.utils.StringUtils;
import com.ryderbelserion.fusion.velocity.api.commands.objects.AbstractVelocityCommand;
import com.ryderbelserion.fusion.velocity.api.commands.objects.AbstractVelocityContext;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandPlugins extends AbstractVelocityCommand {

    private final ProxyServer server;
    private final PluginManager pluginManager;

    public CommandPlugins(@NotNull final ProxyServer server) {
        this.server = server;
        this.pluginManager = this.server.getPluginManager();
    }

    @Override
    public void execute(@NotNull final AbstractVelocityContext context) {
        final List<String> plugins = List.of(
                "<red>===== Active Plugins =====</red>",
                "{plugins}",
                "<red>=========================</red>"
        );
        
        final StringBuilder builder = new StringBuilder();

        this.pluginManager.getPlugins().forEach(plugin -> builder.append(plugin.getDescription().getName()).append("\n"));

        builder.append("\n");

        for (final String plugin : plugins) {
            context.getCommandSender().sendRichMessage(plugin.replaceAll("\\{plugins}", builder.toString()));
        }
    }

    @Override
    public boolean requirement(@NotNull final CommandSource sender) {
        return sender.hasPermission(getPermissions().getFirst());
    }

    @Override
    public @NotNull final List<String> getPermissions() {
        return List.of("fusion.plugins");
    }

    @Override
    public @NotNull BrigadierCommand build() {
        return new BrigadierCommand(BrigadierCommand.literalArgumentBuilder("plugins")
                .requires(this::requirement).executes(context -> {
                    execute(new AbstractVelocityContext(context));

                    return Command.SINGLE_SUCCESS;
                }).build());
    }
}