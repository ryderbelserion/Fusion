package com.ryderbelserion.fusion.commands;

import com.ryderbelserion.fusion.kyori.commands.api.annotations.Flower;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.other.Permission;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.subs.Leaf;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.Tree;
import com.ryderbelserion.fusion.kyori.commands.api.objects.AbstractCommand;
import com.ryderbelserion.fusion.commands.types.SubCommand;
import org.bukkit.entity.Player;

@Tree(value = "fusion", desc = "The base command for Fusion!")
@Permission(permission = "fusion.use")
public class BaseCommand extends AbstractCommand {

    public BaseCommand() {
        addCommand(new SubCommand());
    }

    @Flower
    @Permission(permission = "fusion.execute")
    public void execute(Player player) {
        player.sendRichMessage("<red>This is the default command.");
    }

    @Leaf(value = "take")
    @Permission(permission = "fusion.take")
    public void take() {
        System.out.println("This is the take command.");
    }

    @Leaf(value = "give")
    @Permission(permission = "fusion.give")
    public void give() {
        System.out.println("This is the give command.");
    }
}