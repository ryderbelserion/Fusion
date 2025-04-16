package com.ryderbelserion.fusion.discord;

import net.dv8tion.jda.api.JDA;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import java.nio.file.Path;
import java.util.Optional;

public class Starter {

    public static void main(final String[] args) {
        final Path path = Path.of("examples/discord/fusion");

        final FusionDiscord discord = new FusionDiscord(ComponentLogger.logger("Fusion"), path);

        final Optional<JDA> instance = discord.getJda();

        if (instance.isPresent()) {
            final JDA bot = instance.get();

            final ComponentLogger logger = discord.getLogger();

            logger.warn("Name: {}", bot.getSelfUser().getName());
        }
    }
}