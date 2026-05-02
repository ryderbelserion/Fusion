package com.ryderbelserion.fusion.commands;

import com.ryderbelserion.fusion.commands.api.annotations.Flower;
import com.ryderbelserion.fusion.commands.api.annotations.other.Permission;
import com.ryderbelserion.fusion.commands.api.annotations.subs.Leaf;
import com.ryderbelserion.fusion.commands.api.annotations.Tree;
import com.ryderbelserion.fusion.commands.api.objects.TreeCommand;
import com.ryderbelserion.fusion.commands.types.SubCommand;

@Tree(value = "fusion", description = "The base command for Fusion!")
public class BaseCommand extends TreeCommand {

    public BaseCommand() {
        addCommand(new SubCommand());
    }

    @Flower
    public void execute() {
        System.out.println("The default command.");
    }

    @Leaf(value = "take", weight = 1)
    @Permission(permission = "fusion.take")
    public void take() {
        System.out.println("This is the take command.");
    }

    @Leaf(value = "give", weight = 2)
    @Permission(permission = "fusion.give")
    public void give() {
        System.out.println("This is the give command.");
    }
}