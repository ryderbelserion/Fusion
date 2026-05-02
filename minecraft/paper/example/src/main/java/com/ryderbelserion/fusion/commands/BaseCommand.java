package com.ryderbelserion.fusion.commands;

import com.ryderbelserion.fusion.commands.annotations.Leaf;
import com.ryderbelserion.fusion.commands.annotations.Tree;
import com.ryderbelserion.fusion.commands.api.TreeCommand;
import com.ryderbelserion.fusion.commands.types.SubCommand;

@Tree(value = "fusion", description = "The base command for Fusion!")
public class BaseCommand extends TreeCommand {

    public BaseCommand() {
        addCommand(new SubCommand());
    }

    @Leaf(value = "take", weight = 1)
    public void take() {
        System.out.println("This is the take command.");
    }

    @Leaf(value = "give", weight = 2)
    public void give() {
        System.out.println("This is the give command.");
    }
}