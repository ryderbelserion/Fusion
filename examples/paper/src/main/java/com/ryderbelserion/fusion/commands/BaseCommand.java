package com.ryderbelserion.fusion.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.Fusion;
import com.ryderbelserion.fusion.commands.types.CommandBypass;
import com.ryderbelserion.fusion.commands.types.CommandItem;
import com.ryderbelserion.fusion.commands.types.CommandReload;
import com.ryderbelserion.fusion.paper.api.commands.objects.AbstractPaperCommand;
import com.ryderbelserion.fusion.paper.api.commands.objects.AbstractPaperContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class BaseCommand extends AbstractPaperCommand {

    private final Fusion plugin = JavaPlugin.getPlugin(Fusion.class);

    @Override
    public void execute(@NotNull final AbstractPaperContext context) {
        context.getPlayer().sendRichMessage("<red>This is the base command!");
    }

    @Override
    public final boolean requirement(@NotNull final CommandSourceStack source) {
        return source.getSender().hasPermission(getPermissions().getFirst());
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("fusion")
                .requires(this::requirement)
                .executes(context -> {
                    execute(new AbstractPaperContext(context));

                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                }).build();
    }

    @Override
    public @NotNull final PermissionDefault getPermissionMode() {
        return PermissionDefault.OP;
    }

    @Override
    public @NotNull final List<String> getPermissions() {
        return List.of("fusion.use");
    }

    @Override
    public @NotNull final List<AbstractPaperCommand> getChildren() {
        return List.of(new CommandReload(), new CommandItem(), new CommandBypass());
    }
}