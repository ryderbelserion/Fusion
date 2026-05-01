package com.ryderbelserion.fusion.commands.types;

import com.ryderbelserion.fusion.commands.annotations.Leaf;

@Leaf(value = "sub", weight = 3)
public class SubCommand {

    @Leaf(value = "help", weight = 4)
    public void help() {

    }

    @Leaf(value = "balance", weight = 5)
    public void balance() {

    }
}