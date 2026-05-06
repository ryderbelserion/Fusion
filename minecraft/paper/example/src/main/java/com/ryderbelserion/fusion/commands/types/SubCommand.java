package com.ryderbelserion.fusion.commands.types;

import com.ryderbelserion.fusion.commands.api.annotations.Flower;
import com.ryderbelserion.fusion.commands.api.annotations.other.Permission;
import com.ryderbelserion.fusion.commands.api.annotations.subs.Branch;
import com.ryderbelserion.fusion.commands.api.annotations.subs.Leaf;

@Branch(value = "sub")
@Permission(permission = "fusion.sub")
public class SubCommand {

    @Flower
    public void flower() {
        System.out.println("This is the default sub command.");
    }

    @Leaf(value = "help")
    @Permission(permission = "fusion.help")
    public void help() {
        System.out.println("This is the help command.");
    }

    @Leaf(value = "balance")
    @Permission(permission = "fusion.balance")
    public void balance() {
        System.out.println("This is the balance command.");
    }
}