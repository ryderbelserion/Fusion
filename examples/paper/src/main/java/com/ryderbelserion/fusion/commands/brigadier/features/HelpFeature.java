package com.ryderbelserion.fusion.commands.brigadier.features;

import com.ryderbelserion.core.api.commands.v2.annotations.Command;
import com.ryderbelserion.core.api.commands.v2.annotations.Permission;
import com.ryderbelserion.core.api.commands.v2.enums.PermissionMode;
import org.bukkit.command.CommandSender;

public class HelpFeature implements BaseFeature {

    @Command("help")
    @Permission(value = "redstonepvp.help", mode = PermissionMode.TRUE, description = "Allows you to use the /redstonepvp help command!")
    public void help(final CommandSender sender) {
        sender.sendRichMessage("<red>This is the help command.");
    }
}