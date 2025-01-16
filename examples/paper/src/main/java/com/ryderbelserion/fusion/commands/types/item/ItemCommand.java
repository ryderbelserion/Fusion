package com.ryderbelserion.fusion.commands.types.item;

import com.ryderbelserion.fusion.commands.types.BaseCommand;
import com.ryderbelserion.paper.builder.items.modern.ItemBuilder;
import com.ryderbelserion.paper.builder.items.modern.types.PatternBuilder;
import com.ryderbelserion.paper.util.PaperMethods;
import dev.triumphteam.cmd.core.annotations.Command;
import dev.triumphteam.cmd.core.annotations.CommandFlags;
import dev.triumphteam.cmd.core.annotations.Flag;
import dev.triumphteam.cmd.core.argument.keyed.Flags;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public class ItemCommand extends BaseCommand {

    @Command("item")
    @CommandFlags({
            @Flag(flag = "i", longFlag = "item", argument = String.class, suggestion = "items"),

            // banner / color arguments
            @Flag(flag = "c", longFlag = "color", argument = String.class, suggestion = "colors"),
            @Flag(flag = "p", longFlag = "pattern", argument = String.class, suggestion = "patterns"),

            // generic arguments
            @Flag(flag = "a", longFlag = "amount", argument = Integer.class),

            @Flag(flag = "cu", longFlag = "custom", argument = String.class, suggestion = "custom-items"),
    })
    public void item(final Player player, Flags flag) {
        if (!flag.hasFlag("i")) {
            player.sendRichMessage("<red>The item flag is not detected!</red>");

            return;
        }

        ItemBuilder itemBuilder = null;

        if (flag.hasFlag("cu") && !flag.hasFlag("i")) {
            final @NotNull Optional<String> item = flag.getFlagValue("cu");

            if (item.isPresent()) {
                itemBuilder = ItemBuilder.from(item.get());
            }
        } else if (flag.hasFlag("i")) {
            final @NotNull Optional<String> item = flag.getFlagValue("i");

            if (item.isPresent()) {
                final ItemType itemType = PaperMethods.getItemType(item.get());

                itemBuilder = ItemBuilder.from(itemType != null ? itemType : ItemType.STONE);
            }
        }

        if (itemBuilder == null) return;

        if (flag.hasFlag("a")) {
            final @NotNull Optional<Integer> value = flag.getFlagValue("a", Integer.class);

            value.ifPresent(itemBuilder::setAmount);
        }

        if (flag.hasFlag("c") && !flag.hasFlag("p") && itemBuilder.isDyeable()) {
            final @NotNull Optional<String> value = flag.getFlagValue("c");

            if (value.isPresent()) {
                itemBuilder.setColor(value.get());
            }
        }

        if (flag.hasFlag("c") && flag.hasFlag("p") && itemBuilder.isShield()) {
            final @NotNull Optional<String> color = flag.getFlagValue("c");
            final @NotNull Optional<String> pattern = flag.getFlagValue("p");

            if (color.isPresent() && pattern.isPresent()) {
                final PatternBuilder builder = itemBuilder.asPatternBuilder();

                builder.addPattern(pattern.get(), color.get());

                builder.build();
            }
        }

        itemBuilder.addPlaceholder("{name}", player.getName())
                .addPlaceholder("experience", String.valueOf(player.getLevel()))
                .addPlaceholder("uuid", player.getUniqueId().toString())
                .addPlaceholder("{uuid}", player.getUniqueId().toString())
                .setDisplayName("<red>This is an item with {name} and <experience>".replaceAll("\\{", "<").replaceAll("}", ">"))
                .addDisplayLore("<green>This is a line with <uuid>.")
                .addDisplayLore("<yellow>This is another line or {uuid}.".replaceAll("\\{", "<").replaceAll("}", ">"));

        itemBuilder.addItemToInventory(player, player.getInventory());
    }
}