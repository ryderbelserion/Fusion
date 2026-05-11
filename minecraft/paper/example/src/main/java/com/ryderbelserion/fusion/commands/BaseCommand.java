package com.ryderbelserion.fusion.commands;

import com.ryderbelserion.fusion.kyori.commands.api.annotations.Flower;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.other.Permission;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.subs.Leaf;
import com.ryderbelserion.fusion.kyori.commands.api.annotations.Tree;
import com.ryderbelserion.fusion.kyori.commands.api.objects.AbstractCommand;
import com.ryderbelserion.fusion.commands.types.SubCommand;

@Tree(value = "fusion", desc = "The base command for Fusion!")
@Permission(permission = "fusion.use")
public class BaseCommand extends AbstractCommand {

    public BaseCommand() {
        addCommand(new SubCommand());
    }

    @Flower
    @Permission(permission = "fusion.execute")
    public void execute() {
        System.out.println("<red>This is the default command.");
    }

    @Leaf(value = "take", desc = "The take command")
    @Permission(permission = "fusion.take")
    public void take() {
        System.out.println("This is the take command.");
    }

    @Leaf(value = "give", desc = "The give command")
    @Permission(permission = "fusion.give")
    public void give() {
        System.out.println("This is the give command.");
    }
}