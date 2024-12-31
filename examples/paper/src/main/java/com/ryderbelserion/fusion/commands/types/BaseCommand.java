package com.ryderbelserion.fusion.commands.types;

import com.ryderbelserion.fusion.FusionPlugin;
import dev.triumphteam.cmd.core.annotations.Command;

@Command(value = "fusion")
public abstract class BaseCommand {

    protected final FusionPlugin plugin = FusionPlugin.getPlugin();

}