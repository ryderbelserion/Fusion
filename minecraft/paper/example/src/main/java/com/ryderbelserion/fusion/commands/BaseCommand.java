package com.ryderbelserion.fusion.commands;

import com.ryderbelserion.fusion.kyori.commands.api.annotations.Flower;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.other.Permission;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.subs.Branch;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.subs.Leaf;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.Tree;
import com.ryderbelserion.fusion.kyori.commands.api.objects.api.AbstractCommand;
import com.ryderbelserion.fusion.commands.types.SubCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Tree(value = "fusion", desc = "The base command for Fusion!")
@Permission(permission = "fusion.use")
public class BaseCommand extends AbstractCommand<CommandSourceStack> {

    public BaseCommand() {
        addCommand(new SubCommand());
    }

    @Flower
    @Permission(permission = "fusion.execute")
    public void execute(CommandSender sender) {
        sender.sendRichMessage("<yellow>This is the base command!");
    }

    @Leaf(value = "take", desc = "The take command")
    @Permission(permission = "fusion.take")
    public void take(Player player) {
        player.sendRichMessage("<red>This is the take command.");
    }

    @Leaf(value = "give", desc = "The give command")
    @Permission(permission = "fusion.give")
    public void give(Player player) {
        player.sendRichMessage("<red>This is the give command.");
    }

    @Branch(value = "test")
    @Permission(permission = "fusion.test")
    public static class TestCommand {

        @Flower
        public void flower(Player player) {
            player.sendRichMessage("<red>This is the default test command.");
        }

        @Leaf(value = "helpme", desc = "The helpme command")
        @Permission(permission = "fusion.helpme")
        public void helpme(Player player) {
            player.sendRichMessage("<red>This is the helpme command.");
        }

        @Leaf(value = "bank", desc = "The bank command")
        @Permission(permission = "fusion.bank")
        public void bank(Player player) {
            player.sendRichMessage("<red>This is the bank command.");
        }
    }
}