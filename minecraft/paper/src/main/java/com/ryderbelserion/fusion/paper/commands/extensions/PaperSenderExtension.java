package com.ryderbelserion.fusion.paper.commands.extensions;

import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.commands.PaperCommandManager;
import com.ryderbelserion.fusion.kyori.commands.api.senders.MetaKeys;
import com.ryderbelserion.fusion.kyori.commands.api.senders.objects.SenderExtension;
import com.ryderbelserion.fusion.kyori.commands.api.senders.results.ValidationResult;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

public class PaperSenderExtension implements SenderExtension<CommandSourceStack> {

    private final FusionPaper fusion = (FusionPaper) FusionProvider.getInstance();

    private final PaperCommandManager commandManager = this.fusion.getCommandManager();

    @Override
    public @NotNull final ValidationResult<?> validate(@NotNull final Class<?> target, @NotNull final CommandSourceStack source) {
        final CommandSender sender = source.getSender();

        if (Player.class.isAssignableFrom(target) && (!(sender instanceof Player))) {
            return invalid(this.commandManager.getMessage(MetaKeys.must_be_player).orElse(""));
        }

        if (ConsoleCommandSender.class.isAssignableFrom(target) && !(sender instanceof ConsoleCommandSender)) {
            return invalid(this.commandManager.getMessage(MetaKeys.must_be_console_sender).orElse(""));
        }

        return valid();
    }

    @Override
    public @NotNull final Set<Class<?>> getSenders() {
        return Set.of(CommandSender.class, ConsoleCommandSender.class, Player.class);
    }
}