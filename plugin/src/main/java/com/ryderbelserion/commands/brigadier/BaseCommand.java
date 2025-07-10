package com.ryderbelserion.commands.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.CrazyCrates;
import com.ryderbelserion.commands.brigadier.types.admin.CommandReload;
import com.ryderbelserion.fusion.paper.api.commands.objects.PaperCommand;
import com.ryderbelserion.fusion.paper.api.commands.objects.PaperCommandContext;
import com.ryderbelserion.fusion.paper.files.FileManager;
import com.ryderbelserion.fusion.paper.files.types.PaperCustomFile;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.nio.file.Path;
import java.util.List;

public class BaseCommand extends PaperCommand {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final FileManager fileManager = this.plugin.getFileManager();

    @Override
    public void execute(@NotNull final PaperCommandContext context) {
        final PaperCustomFile crate = getCrateConfig("CrateExample.yml");

        if (crate == null) return;

        final YamlConfiguration configuration = crate.getConfiguration();

        context.getCommandSender().sendRichMessage(String.format("<red>Crate Type: <yellow>%s", configuration.getString("Crate.CrateType", "CSGO")));
    }

    @Override
    public final boolean requirement(@NotNull final CommandSourceStack source) {
        final CommandSender sender = source.getSender();

        return sender instanceof ConsoleCommandSender || sender.hasPermission(getPermissions().getFirst());
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("crazycrates").requires(this::requirement)
                .executes(context -> {
                    execute(new PaperCommandContext(context));

                    return Command.SINGLE_SUCCESS;
                }).build();
    }

    @Override
    public @NotNull final List<String> getPermissions() {
        return List.of("crazycrates.use");
    }

    @Override
    public @NotNull final List<PaperCommand> getChildren() {
        return List.of(new CommandReload());
    }

    public @Nullable PaperCustomFile getCrateConfig(final String fileName) {
        final Path path = this.plugin.getDataPath().resolve("crates").resolve(fileName);

        return this.fileManager.getPaperCustomFile(path);
    }
}