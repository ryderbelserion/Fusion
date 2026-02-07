package com.ryderbelserion.fusion.paper.builders.commands;

import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.mojang.AbstractCommand;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.builders.commands.context.PaperCommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public abstract class PaperCommand extends AbstractCommand<CommandSourceStack, PaperCommandContext> {

    private final FusionPaper fusion = (FusionPaper) FusionProvider.getInstance();

}