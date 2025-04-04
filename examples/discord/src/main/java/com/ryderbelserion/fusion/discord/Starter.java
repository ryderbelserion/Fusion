package com.ryderbelserion.fusion.discord;

import ch.qos.logback.classic.Logger;
import com.ryderbelserion.fusion.discord.managers.LoggerManager;
import net.dv8tion.jda.api.JDA;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;
import java.util.Optional;

public class Starter {

    public static void main(final String[] args) {
        final Path path = Path.of("examples/discord/fusion");

        final FusionDiscord discord = new FusionDiscord(path, (Logger) LoggerFactory.getLogger("Fusion"));

        final Optional<JDA> instance = discord.getJda();

        if (instance.isPresent()) {
            final JDA bot = instance.get();

            final LoggerManager logger = discord.getLogger();

            logger.warn("Bot Name: {}", bot.getSelfUser().getName());
        }
    }
}