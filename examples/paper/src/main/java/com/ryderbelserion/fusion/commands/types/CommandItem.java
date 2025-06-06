package com.ryderbelserion.fusion.commands.types;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.fusion.Fusion;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.api.builders.items.ItemBuilder;
import com.ryderbelserion.fusion.paper.api.commands.PaperCommandManager;
import com.ryderbelserion.fusion.paper.api.commands.objects.AbstractPaperCommand;
import com.ryderbelserion.fusion.paper.api.commands.objects.AbstractPaperContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemType;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

public class CommandItem extends AbstractPaperCommand {

    private final Fusion plugin = JavaPlugin.getPlugin(Fusion.class);

    private final FusionPaper paper = this.plugin.getPaper();

    private final PaperCommandManager manager = this.paper.getCommandManager();

    private final FileManager fileManager = this.plugin.getFileManager();

    private final ComponentLogger logger = this.plugin.getComponentLogger();

    private final Path path = this.plugin.getDataPath();

    @Override
    public void execute(@NotNull final AbstractPaperContext context) {
        final Player player = context.getPlayer();

        final ItemBuilder itemBuilder = ItemBuilder.from(ItemType.DIAMOND_SWORD).setDisplayName("<red>{name}, <yes>").addEnchantment("sharpness", 3).hideComponent("enchantments")
                .addPlaceholder("<yes>", player.getUniqueId().toString())
                .addPlaceholder("{name}", player.getName());

        itemBuilder.addItemToInventory(player.getInventory());
    }

    @Override
    public final boolean requirement(@NotNull final CommandSourceStack source) {
        final CommandSender sender = source.getSender();

        return this.manager.hasPermission(source, getPermissionMode(), getPermissions());
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        this.manager.registerPermissions(PermissionDefault.OP, getPermissions());

        return literal().createBuilder().build();
    }

    @Override
    public void unregister() {
        this.manager.unregisterPermissions(getPermissions());
    }

    @Override
    public String[] getPermissions() {
        return new String[]{"fusion.item"};
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("item")
                .requires(this::requirement)
                .executes(context -> {
                    execute(new AbstractPaperContext(context));

                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                }).build();
    }
}