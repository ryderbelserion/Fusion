package com.ryderbelserion.fusion.paper.api.commands.objects;

import com.ryderbelserion.fusion.kyori.commands.objects.AbstractCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPaperCommand extends AbstractCommand<CommandSourceStack, Player, AbstractPaperContext> {

    public @NotNull List<AbstractPaperCommand> getChildren() {
        return new ArrayList<>();
    }
}