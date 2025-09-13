package com.ryderbelserion.commands.brigadier.types.admin;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.CrazyCrates;
import com.ryderbelserion.fusion.core.api.enums.FileType;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.api.commands.objects.PaperCommand;
import com.ryderbelserion.fusion.paper.api.commands.objects.PaperCommandContext;
import com.ryderbelserion.fusion.paper.files.FileManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class CommandReload extends PaperCommand {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final ComponentLogger logger = this.plugin.getComponentLogger();

    private final FusionPaper fusion = this.plugin.getFusion();

    private final FileManager fileManager = this.plugin.getFileManager();

    @Override
    public void execute(@NotNull final PaperCommandContext context) {
        this.fusion.reload();

        this.fileManager.refresh(false).addFolder(this.plugin.getDataPath().resolve("crates"), FileType.PAPER);

        this.fileManager.getCustomFiles().forEach((path, file) -> {
            logger.warn("File: {}", file.isLoaded());
            logger.warn("Path: {}", path);
        });

        context.getCommandSender().sendRichMessage("<red>You have reloaded the plugin!");
    }

    @Override
    public final boolean requirement(@NotNull final CommandSourceStack source) {
        final CommandSender sender = source.getSender();

        return sender instanceof ConsoleCommandSender || sender.hasPermission(getPermissions().getFirst());
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("reload")
                .requires(this::requirement)
                .executes(context -> {
                    execute(new PaperCommandContext(context));

                    return Command.SINGLE_SUCCESS;
                }).build();
    }

    @Override
    public @NotNull final List<String> getPermissions() {
        return List.of("crazycrates.reload");
    }
}