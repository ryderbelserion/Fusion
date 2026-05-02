package com.ryderbelserion.fusion.commands.types;

import com.ryderbelserion.fusion.commands.annotations.Flower;
import com.ryderbelserion.fusion.commands.annotations.subs.Branch;
import com.ryderbelserion.fusion.commands.annotations.subs.Leaf;

@Branch(value = "sub")
public class SubCommand {

    @Flower
    public void flower() {
        System.out.println("This is the default sub command.");
    }

    @Leaf(value = "help", weight = 3)
    public void help() {
        System.out.println("This is the help command.");
    }

    @Leaf(value = "balance", weight = 4)
    public void balance() {
        System.out.println("This is the balance command.");
    }
}