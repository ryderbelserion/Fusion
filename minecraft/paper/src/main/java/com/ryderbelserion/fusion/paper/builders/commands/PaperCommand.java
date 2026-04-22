package com.ryderbelserion.fusion.paper.builders.commands;

import com.ryderbelserion.fusion.mojang.AbstractCommand;
import com.ryderbelserion.fusion.paper.builders.commands.context.PaperCommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public abstract class PaperCommand extends AbstractCommand<PaperCommand, CommandSourceStack, PaperCommandContext> {}