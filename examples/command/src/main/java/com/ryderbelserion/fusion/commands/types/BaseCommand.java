package com.ryderbelserion.fusion.commands.types;

import com.ryderbelserion.core.commands.annotation.Command;
import com.ryderbelserion.fusion.FusionPlugin;

@Command(value = "fusion")
public abstract class BaseCommand {

    protected final FusionPlugin plugin = FusionPlugin.getPlugin();

}