package com.ryderbelserion.fusion.commands.types;

import ch.jalu.configme.SettingsManager;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.Fusion;
import com.ryderbelserion.fusion.config.Config;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.types.JaluCustomFile;
import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.api.commands.PaperCommandManager;
import com.ryderbelserion.fusion.paper.api.commands.objects.AbstractPaperCommand;
import com.ryderbelserion.fusion.paper.api.commands.objects.AbstractPaperContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

public class CommandReload extends AbstractPaperCommand {

    private final Fusion plugin = JavaPlugin.getPlugin(Fusion.class);

    private final FusionPaper paper = this.plugin.getPaper();

    private final PaperCommandManager manager = this.paper.getCommandManager();

    private final FileManager fileManager = this.plugin.getFileManager();

    private final ComponentLogger logger = this.plugin.getComponentLogger();

    private final Path path = this.plugin.getDataPath();

    @Override
    public void execute(@NotNull final AbstractPaperContext context) {
        this.fileManager.refresh(false);

        final YamlCustomFile yaml = this.fileManager.getYamlFile(path.resolve("actions.yml"));

        if (yaml != null) {
            this.logger.warn("Yaml File: {}", yaml.getConfiguration().node("value").getBoolean(false));
        }

        final JaluCustomFile jalu = this.fileManager.getJaluFile(path.resolve("config.yml"));

        if (jalu != null) {
            final SettingsManager config = jalu.getConfiguration();

            this.logger.warn("Prefix: {}", config.getProperty(Config.prefix));
        }
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
        return Commands.literal("reload")
                .requires(this::requirement)
                .executes(context -> {
                    execute(new AbstractPaperContext(context));

                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                }).build();
    }
}