package com.ryderbelserion.fusion.commands.types.item;

import com.ryderbelserion.fusion.commands.types.BaseCommand;
import dev.triumphteam.cmd.core.annotations.Command;
import org.bukkit.command.CommandSender;

public class PurgeCommand extends BaseCommand {

    @Command("purge")
    public void test(final CommandSender player) {
        this.plugin.purge();
    }
}