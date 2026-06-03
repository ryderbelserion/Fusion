package com.ryderbelserion.fusion.commands.types;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.Fusion;
import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.core.api.registry.message.MessageRegistry;
import com.ryderbelserion.fusion.core.api.registry.message.adapter.interfaces.IMessageAdapter;
import com.ryderbelserion.fusion.kyori.permissions.PermissionContext;
import com.ryderbelserion.fusion.kyori.permissions.enums.PermissionType;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.builders.commands.PaperCommand;
import com.ryderbelserion.fusion.paper.builders.commands.context.PaperCommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.key.Key;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.papermc.paper.command.brigadier.Commands.argument;

public class ReloadCommand extends PaperCommand {

    private final Fusion fusion;

    public ReloadCommand(@NonNull final Fusion fusion) {
        this.fusion = fusion;
    }

    @Override
    public void run(@NonNull final PaperCommandContext context) {
        final CommandSender sender = context.getSender();

        final FusionPaper paper = this.fusion.getFusion();

        final MessageRegistry registry = paper.getMessageRegistry();

        final Map<FusionKey, IMessageAdapter> messages = registry.getMessages().get(registry.getDefaultKey());

        final FusionKey key = FusionKey.key(paper.getNamespace(), "reload_plugin");

        paper.log(Level.WARNING, "Contains: %s", messages.containsKey(key));

        messages.forEach(((fusionKey, adapter) -> {
            paper.log(Level.WARNING, "Adapter: %s", fusionKey.asString());
            paper.log(Level.WARNING, "Message: %s", adapter.getValue());
        }));

        final Optional<IMessageAdapter> msg = registry.getMessage(key);

        if (msg.isEmpty()) {
            paper.log(Level.WARNING, "Message is empty!");

            return;
        }

        sender.sendRichMessage(msg.get().getValue());

        sender.sendRichMessage("<yellow>%s Amount</yellow>".formatted(context.getIntegerArgument("amount").orElse(30)));
    }

    @Override
    public @NonNull final LiteralCommandNode<CommandSourceStack> literal() {
        final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("reload").requires(this::requirement);

        final RequiredArgumentBuilder<CommandSourceStack, Integer> arg1 = argument("amount", IntegerArgumentType.integer(1, 64)).executes(context -> {
            run(new PaperCommandContext(context));

            return Command.SINGLE_SUCCESS;
        });

        return root.then(arg1).executes(context -> {
            run(new PaperCommandContext(context));

            return Command.SINGLE_SUCCESS;
        }).build();
    }

    @Override
    public @NonNull final List<PermissionContext> getPermissions() {
        return List.of(
                new PermissionContext(
                        "fusion.reload",
                        "The reload command for Fusion!",
                        PermissionType.TRUE
                )
        );
    }

    @Override
    public final boolean requirement(@NonNull final CommandSourceStack context) {
        return context.getSender().hasPermission(getPermissions().getFirst().getPermission());
    }
}