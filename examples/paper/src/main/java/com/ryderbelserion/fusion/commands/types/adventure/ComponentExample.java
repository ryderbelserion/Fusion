package com.ryderbelserion.fusion.commands.types.adventure;

import com.ryderbelserion.fusion.commands.types.BaseCommand;
import com.ryderbelserion.paper.fusion.builder.api.ComponentBuilder;
import dev.triumphteam.cmd.core.annotations.Command;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;

public class ComponentExample extends BaseCommand {

    @Command("component")
    public void component(final Player player) {
        final ComponentBuilder builder = new ComponentBuilder("<red>This is a placeholder test using {name} with <experience> levels.</red> <anilist_link><gold>Please check out my anime:</gold></anilist_link>");

        builder.addPlaceholderResolver("{name}", player.getName());
        builder.addPlaceholderResolver("experience", String.valueOf(player.getLevel()));

        builder.addClickResolver("anilist_link", "https://anilist.co/user/RyderBelserion/animelist", ClickEvent.Action.OPEN_URL);

        player.sendMessage(builder.asComponent(player));

        /*Component component = this.plugin.getApi().getFusion().placeholders(player, "<red>This is a placeholder test using {name} with <experience> levels".replaceAll("\\{", "<").replaceAll("}", ">"), new HashMap<>() {{
            put("{name}", player.getName());
            put("experience", String.valueOf(player.getLevel()));
        }});

        player.sendMessage(component);*/
    }
}