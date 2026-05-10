package com.ryderbelserion.fusion.paper.commands.extensions;

import com.ryderbelserion.fusion.paper.commands.PaperCommandManager;
import com.ryderbelserion.fusion.kyori.commands.api.senders.MetaKeys;
import com.ryderbelserion.fusion.kyori.commands.api.senders.objects.SenderExtension;
import com.ryderbelserion.fusion.kyori.commands.api.senders.results.ValidationResult;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

public class PaperSenderExtension implements SenderExtension<Audience> {

    private final PaperCommandManager commandManager;

    public PaperSenderExtension(@NotNull final PaperCommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public @NotNull final ValidationResult<?> validate(@NotNull final Class<?> target, @NotNull final Audience sender) {
        if (Player.class.isAssignableFrom(target) && (!(sender instanceof Player))) {
            return invalid(this.commandManager.getMessage(MetaKeys.must_be_player).orElse(""));
        }

        if (ConsoleCommandSender.class.isAssignableFrom(target) && !(sender instanceof ConsoleCommandSender)) {
            return invalid(this.commandManager.getMessage(MetaKeys.must_be_console_sender).orElse(""));
        }

        return valid();
    }

    @Override
    public @NotNull final Set<Class<? extends Audience>> getSenders() {
        return Set.of(CommandSender.class, ConsoleCommandSender.class, Player.class);
    }
}