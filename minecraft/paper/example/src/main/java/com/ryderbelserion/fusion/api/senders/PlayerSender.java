package com.ryderbelserion.fusion.api.senders;

import com.ryderbelserion.fusion.commands.api.args.ISender;
import com.ryderbelserion.fusion.commands.api.args.objects.SenderData;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlayerSender implements ISender<Player, CommandSourceStack> {

    @Override
    public @NotNull SenderData<Player> convert(@NotNull final CommandSourceStack context) {
        org.bukkit.command.CommandSender sender = context.getExecutor();

        if (sender == null) {
            sender = context.getSender();
        }

        if (!(sender instanceof Player player)) {
            return new SenderData<>(0);
        }

        final SenderData data = new SenderData(1);

        data.setSender(player);

        return data;
    }

    @Override
    public @NotNull Class<CommandSourceStack> getSource() {
        return CommandSourceStack.class;
    }

    @Override
    public @NotNull Class<Player> getSender() {
        return Player.class;
    }
}