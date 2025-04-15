package com.ryderbelserion.fusion.discord;

import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.core.api.ConfigKeys;
import net.dv8tion.jda.api.JDA;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

import java.io.File;
import java.nio.file.Path;

public class Starter {

    public static void main(final String[] args) {
        final Path path = Path.of("examples/discord/fusion");

        YamlFileResourceOptions builder = YamlFileResourceOptions.builder().indentationSize(2).build();

        SettingsManagerBuilder.withYamlFile(new File(new File("./"), "fusion.yml"), builder)
                .configurationData(DiscordKeys.class)
                .create();

        /*final FusionDiscord discord = new FusionDiscord(ComponentLogger.logger("Fusion"), path).start();

        final Optional<JDA> instance = discord.getJda();

        if (instance.isPresent()) {
            final JDA bot = instance.get();

            final ComponentLogger logger = discord.getLogger();

            logger.warn("Name: {}", bot.getSelfUser().getName());
        }*/
    }
}