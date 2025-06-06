package com.ryderbelserion.fusion.paper.api.commands.objects;

import com.ryderbelserion.fusion.kyori.commands.objects.AbstractCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

public abstract class AbstractPaperCommand extends AbstractCommand<CommandSourceStack, Player, AbstractPaperContext> {}