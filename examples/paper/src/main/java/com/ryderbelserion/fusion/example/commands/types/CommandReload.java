package com.ryderbelserion.fusion.example.commands.types;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.example.Fusion;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import com.ryderbelserion.fusion.paper.api.commands.objects.AbstractPaperCommand;
import com.ryderbelserion.fusion.paper.api.commands.objects.AbstractPaperContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

public class CommandReload extends AbstractPaperCommand {

    private static final Pattern PATTERN = Pattern.compile("\\{prefix}");

    private final Fusion plugin = JavaPlugin.getPlugin(Fusion.class);

    private final FileManager fileManager = this.plugin.getFileManager();

    private final Path path = this.plugin.getDataPath();

    @Override
    public void execute(@NotNull final AbstractPaperContext context) {
        this.fileManager.refresh(false);

        final CommandSender sender = context.getCommandSender();

        final YamlCustomFile yaml = this.fileManager.getYamlFile(path.resolve("actions.yml"));

        String prefix = "";

        if (yaml != null) {
            final CommentedConfigurationNode config = yaml.getConfiguration();

            if (config.node("value").getBoolean(false)) {
                sender.sendRichMessage(PATTERN.matcher("{prefix} <yellow>The config option is true!").replaceAll(prefix));
            }
        }
    }

    @Override
    public final boolean requirement(@NotNull final CommandSourceStack source) {
        return source.getSender().hasPermission(getPermissions().getFirst());
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("reload")
                .requires(this::requirement)
                .executes(context -> {
                    execute(new AbstractPaperContext(context));

                    return Command.SINGLE_SUCCESS;
                }).build();
    }

    @Override
    public @NotNull final PermissionDefault getPermissionMode() {
        return PermissionDefault.OP;
    }

    @Override
    public @NotNull final List<String> getPermissions() {
        return List.of("fusion.reload");
    }
}