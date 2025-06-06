package com.ryderbelserion.fusion.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.Fusion;
import com.ryderbelserion.fusion.commands.types.CommandItem;
import com.ryderbelserion.fusion.commands.types.CommandReload;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.api.commands.PaperCommandManager;
import com.ryderbelserion.fusion.paper.api.commands.objects.AbstractPaperCommand;
import com.ryderbelserion.fusion.paper.api.commands.objects.AbstractPaperContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class BaseCommand extends AbstractPaperCommand {

    private final Fusion plugin = JavaPlugin.getPlugin(Fusion.class);

    private final FusionPaper paper = this.plugin.getPaper();

    private final PaperCommandManager manager = this.paper.getCommandManager();

    @Override
    public void execute(@NotNull final AbstractPaperContext context) {
        context.getPlayer().sendRichMessage("<red>This is the base command!");
    }

    @Override
    public final boolean requirement(@NotNull final CommandSourceStack source) {
        final CommandSender sender = source.getSender();

        return this.manager.hasPermission(source, getPermissionMode(), getPermissions());
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> build() {
        this.manager.registerPermissions(PermissionDefault.OP, getPermissions());

        return literal().createBuilder().build();
    }

    @Override
    public void unregister() {
        this.manager.unregisterPermissions(getPermissions());
    }

    @Override
    public @NotNull final String[] getPermissions() {
        return new String[]{"fusion.use"};
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("fusion")
                .requires(this::requirement)
                .executes(context -> {
                    execute(new AbstractPaperContext(context));

                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                }).build();
    }

    @Override
    public @NotNull final List<AbstractPaperCommand> getChildren() {
        return List.of(new CommandReload(), new CommandItem());
    }
}