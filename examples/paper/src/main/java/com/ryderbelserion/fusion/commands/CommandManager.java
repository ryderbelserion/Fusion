package com.ryderbelserion.fusion.commands;

import com.nexomc.nexo.api.NexoItems;
import com.ryderbelserion.fusion.FusionPlugin;
import com.ryderbelserion.fusion.commands.types.adventure.ComponentExample;
import com.ryderbelserion.fusion.commands.types.item.ItemCommand;
import com.ryderbelserion.fusion.commands.types.item.PurgeCommand;
import com.ryderbelserion.fusion.commands.types.item.TestCommand;
import com.ryderbelserion.fusion.paper.api.enums.Support;
import com.ryderbelserion.fusion.paper.utils.ItemUtils;
import dev.lone.itemsadder.api.CustomStack;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.core.suggestion.SuggestionKey;
import io.papermc.paper.registry.RegistryKey;
import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private static final FusionPlugin plugin = FusionPlugin.getPlugin();

    private static final BukkitCommandManager<CommandSender> manager = BukkitCommandManager.create(plugin);

    public static void load() {
        manager.registerSuggestion(SuggestionKey.of("custom-items"), (sender, context) -> new ArrayList<>() {{
            if (Support.items_adder.isEnabled()) {
                addAll(CustomStack.getNamespacedIdsInRegistry());
            }

            if (Support.oraxen.isEnabled()) {
                addAll(OraxenItems.getNames());
            }

            if (Support.nexo.isEnabled()) {
                addAll(NexoItems.names());
            }
        }});

        manager.registerSuggestion(SuggestionKey.of("items"), (sender, context) -> new ArrayList<>() {{
            ItemUtils.getRegistryAccess().getRegistry(RegistryKey.ITEM).stream().forEach(registry -> {
                add(registry.key().value());
            });
        }});

        manager.registerSuggestion(SuggestionKey.of("patterns"), (sender, context) -> new ArrayList<>() {{
            ItemUtils.getRegistryAccess().getRegistry(RegistryKey.BANNER_PATTERN).stream().forEach(registry -> {
                add(registry.key().value());
            });
        }});

        manager.registerSuggestion(SuggestionKey.of("colors"), (sender, context) -> new ArrayList<>() {{
            addAll(List.of(
                    "aqua",
                    "black",
                    "blue",
                    "fuchsia",
                    "gray",
                    "green",
                    "lime",
                    "maroon",
                    "navy",
                    "olive",
                    "orange",
                    "purple",
                    "red",
                    "silver",
                    "teal",
                    "yellow",
                    "white"
            ));
        }});

        List.of(
                new ComponentExample(),

                new PurgeCommand(),

                new ItemCommand(),
                new TestCommand()
        ).forEach(manager::registerCommand);
    }
}