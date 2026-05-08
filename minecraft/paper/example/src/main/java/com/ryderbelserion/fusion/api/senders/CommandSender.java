package com.ryderbelserion.fusion.api.senders;

import com.ryderbelserion.fusion.commands.api.args.ISender;
import com.ryderbelserion.fusion.commands.api.args.objects.SenderData;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.NotNull;

public final class CommandSender implements ISender<org.bukkit.command.CommandSender, CommandSourceStack> {

    @Override
    public @NotNull SenderData<org.bukkit.command.CommandSender> convert(@NotNull final CommandSourceStack context) {
        org.bukkit.command.CommandSender sender = context.getExecutor();

        if (sender == null) {
            sender = context.getSender();
        }

        final SenderData data = new SenderData(1);

        data.setSender(sender);

        return data;
    }

    @Override
    public @NotNull Class<org.bukkit.command.CommandSender> getSender() {
        return org.bukkit.command.CommandSender.class;
    }

    @Override
    public @NotNull Class<CommandSourceStack> getSource() {
        return CommandSourceStack.class;
    }
}