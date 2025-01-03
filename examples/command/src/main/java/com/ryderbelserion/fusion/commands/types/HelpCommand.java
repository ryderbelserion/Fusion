package com.ryderbelserion.fusion.commands.types;

import com.ryderbelserion.core.commands.annotation.Command;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;

public class HelpCommand extends BaseCommand {

    @Command // default
    public void execute(final CommandSourceStack source) {
        final CommandSender sender = source.getSender();

        sender.sendRichMessage("<red>This is the base command.");
    }

    @Command(value = "help")
    public void help(final CommandSourceStack source) {
        final CommandSender sender = source.getSender();

        sender.sendRichMessage("<red>This is a help command.");
    }

    @Command(value = "text")
    public void text(final CommandSourceStack source) {
        final CommandSender sender = source.getSender();

        sender.sendRichMessage("<red>This is a text command.");
    }
}