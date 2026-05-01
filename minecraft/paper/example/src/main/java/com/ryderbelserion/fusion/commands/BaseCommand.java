package com.ryderbelserion.fusion.commands;

import com.ryderbelserion.fusion.commands.annotations.Leaf;
import com.ryderbelserion.fusion.commands.annotations.Origin;
import com.ryderbelserion.fusion.commands.api.OriginCommand;
import com.ryderbelserion.fusion.commands.types.SubCommand;

@Origin(value = "fusion", description = "The base command for Fusion!")
public class BaseCommand extends OriginCommand {

    public BaseCommand() {
        addCommand(new SubCommand());
    }

    @Leaf(value = "take", weight = 1)
    public void take() {

    }

    @Leaf(value = "give", weight = 2)
    public void give() {

    }
}