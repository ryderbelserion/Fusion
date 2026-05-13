package com.ryderbelserion.fusion.paper.commands.extensions;

import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.core.api.registry.message.MessageRegistry;
import com.ryderbelserion.fusion.kyori.commands.api.objects.meta.LocaleMeta;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.kyori.commands.api.senders.MetaKeys;
import com.ryderbelserion.fusion.kyori.commands.api.senders.objects.SenderExtension;
import com.ryderbelserion.fusion.kyori.commands.api.senders.results.ValidationResult;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.NoSuchElementException;
import java.util.Set;

public class PaperSenderExtension implements SenderExtension.Default<CommandSourceStack> {

    private final FusionPaper fusion = (FusionPaper) FusionProvider.getInstance();

    private final MessageRegistry registry = this.fusion.getMessageRegistry();

    @Override
    public @NotNull final ValidationResult<String> validate(@NotNull final Class<?> target, @NotNull final CommandSourceStack source) {
        final CommandSender sender = source.getSender();

        final LocaleMeta meta = new LocaleMeta(sender);

        if (Player.class.isAssignableFrom(target) && (!(sender instanceof Player))) {
            return invalid(this.registry.getMessageByLocale(meta.getLocale(), MetaKeys.must_be_player).orElseThrow(() -> new NoSuchElementException("Could not find %s".formatted(MetaKeys.must_be_player.asString()))).getValue());
        }

        if (ConsoleCommandSender.class.isAssignableFrom(target) && !(sender instanceof ConsoleCommandSender)) {
            return invalid(this.registry.getMessageByLocale(meta.getLocale(), MetaKeys.must_be_console_sender).orElseThrow(() -> new NoSuchElementException("Could not find %s".formatted(MetaKeys.must_be_console_sender.asString()))).getValue());
        }

        return valid();
    }

    @Override
    public @NotNull Object map(@NotNull final Class<?> type, @NotNull final CommandSourceStack source) {
        final CommandSender sender = source.getSender();

        if (Player.class.isAssignableFrom(type) && sender instanceof Player player) {
            return player;
        }

        if (ConsoleCommandSender.class.isAssignableFrom(type) && sender instanceof ConsoleCommandSender console) {
            return console;
        }

        return sender;
    }

    @Override
    public boolean hasPermission(@NotNull final CommandSourceStack source, @NotNull final String permission) {
        return this.fusion.hasPermission(source.getSender(), permission);
    }

    @Override
    public void sendMessage(@NotNull final CommandSourceStack source, @NotNull final String message) {
        final CommandSender sender = source.getSender();

        sender.sendMessage(this.fusion.asComponent(sender, message));
    }

    @Override
    public @NotNull final Set<Class<?>> getSenders() {
        return Set.of(CommandSender.class, ConsoleCommandSender.class, Player.class);
    }
}