package com.ryderbelserion.paper.commands.context;

import com.mojang.brigadier.context.CommandContext;
import com.ryderbelserion.core.api.commands.context.CommandInfo;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A command data class to provide some abstraction to brigadier commands
 *
 * @author Ryder Belserion
 * @version 0.1.0
 * @since 0.1.0
 */
public class PaperCommandInfo extends CommandInfo<CommandSourceStack> {

    private final CommandContext<CommandSourceStack> context;

    /**
     * A constructor to apply the command context
     *
     * @param context {@link CommandContext}
     * @since 0.1.0
     */
    public PaperCommandInfo(@NotNull final CommandContext<CommandSourceStack> context) {
        super(context);

        this.context = context;
    }

    /**
     * Gets the {@link CommandSender} from the {@link CommandSourceStack}.
     *
     * @return {@link CommandSender}
     * @since 0.1.0
     */
    public @NotNull final CommandSender getCommandSender() {
        return getSource().getSender();
    }

    /**
     * Gets the {@link Player} from the {@link CommandSourceStack}.
     *
     * @return {@link Player}
     * @since 0.1.0
     */
    public @NotNull final Player getPlayer() {
        return (Player) getCommandSender();
    }

    /**
     * Checks if the {@link CommandSender} is a {@link Player}
     *
     * @return true or false
     * @since 0.1.0
     */
    public final boolean isPlayer() {
        return getCommandSender() instanceof Player;
    }

    /**
     * Get the {@link CommandContext}.
     *
     * @return {@link CommandContext}
     * @since 0.1.0
     */
    public final CommandContext<CommandSourceStack> getContext() {
        return this.context;
    }
}