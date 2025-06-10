package com.ryderbelserion.fusion.example.commands.types;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.example.Fusion;
import com.ryderbelserion.fusion.example.enums.BypassType;
import com.ryderbelserion.fusion.paper.api.commands.objects.AbstractPaperCommand;
import com.ryderbelserion.fusion.paper.api.commands.objects.AbstractPaperContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class CommandBypass extends AbstractPaperCommand {

    private final Fusion plugin = JavaPlugin.getPlugin(Fusion.class);

    private final ComponentLogger logger = this.plugin.getComponentLogger();

    @Override
    public void execute(@NotNull final AbstractPaperContext context) {
        if (!context.isPlayer()) {
            context.getCommandSender().sendRichMessage("<red>You must be a player to use this command!</red>");

            return;
        }

        context.getPlayer().sendRichMessage("<green>You succeeded!</green>");
    }

    @Override
    public final boolean requirement(@NotNull final CommandSourceStack source) {
        return source.getSender().hasPermission(getPermissions().getFirst());
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> build() {
        final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("bypass").requires(this::requirement);

        final RequiredArgumentBuilder<CommandSourceStack, String> arg1 = Commands.argument("bypass_type", StringArgumentType.string()).suggests((ctx, builder) -> {
            for (final BypassType value : BypassType.values()) {
                builder.suggest(value.getName());
            }

            return builder.buildFuture();
        }).executes(context -> {
            execute(new AbstractPaperContext(context));

            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        });

        return root.then(arg1).build();
    }

    @Override
    public @NotNull final PermissionDefault getPermissionMode() {
        return PermissionDefault.OP;
    }

    @Override
    public @NotNull final List<String> getPermissions() {
        return List.of("fusion.bypass");
    }
}