package com.ryderbelserion.fusion.commands.types;

import com.ryderbelserion.fusion.kyori.commands.api.annotations.Flower;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.other.Permission;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.subs.Branch;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.subs.Leaf;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

@Branch(value = "sub")
@Permission(permission = "fusion.sub")
public class SubCommand {

    @Flower
    public void flower(CommandSender sender) {
        sender.sendRichMessage("<yellow>This is the default sub command.");
    }

    @Leaf(value = "help", desc = "The help command")
    @Permission(permission = "fusion.help")
    public void help(ConsoleCommandSender sender) {
        sender.sendRichMessage("<green>This is the help command.");
    }

    @Leaf(value = "balance", desc = "The balance command")
    @Permission(permission = "fusion.balance")
    public void balance(ConsoleCommandSender sender) {
        sender.sendRichMessage("<green>This is the balance command.");
    }

    @Branch(value = "smug")
    @Permission(permission = "fusion.smug")
    public class SmugCommand {

        @Flower
        public void flower(ConsoleCommandSender sender) {
            sender.sendRichMessage("<green>This is the default smug command.");
        }

        @Leaf(value = "debug", desc = "The debug command")
        @Permission(permission = "fusion.debug")
        public void debug(ConsoleCommandSender sender) {
            sender.sendRichMessage("<green>This is the debug command.");
        }

        @Leaf(value = "example", desc = "The example command")
        @Permission(permission = "fusion.example")
        public void example(ConsoleCommandSender sender) {
            sender.sendRichMessage("<green>This is the example command.");
        }
    }
}