package com.ryderbelserion.fusion.discord;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.ConfigKeys;
import com.ryderbelserion.fusion.discord.api.DiscordKeys;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.Optional;

public class FusionDiscord extends FusionCore {

    private final JDA jda;

    public FusionDiscord(@NotNull final ComponentLogger logger, @NotNull final Path dataPath) {
        super(consumer -> consumer.configurationData(ConfigKeys.class, DiscordKeys.class), logger, dataPath);

        final String token = this.config.getProperty(DiscordKeys.token);

        final JDA jda = JDABuilder.createDefault(token).build();

        try {
            jda.awaitReady();
        } catch (final InterruptedException exception) {
            getLogger().error("Interrupted while waiting for JDA to start", exception);
        }

        this.jda = jda;
    }

    public final Optional<JDA> getJda() {
        return Optional.ofNullable(this.jda);
    }
}