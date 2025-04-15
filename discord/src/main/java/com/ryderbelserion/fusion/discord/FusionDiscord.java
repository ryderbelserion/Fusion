package com.ryderbelserion.fusion.discord;

import ch.jalu.configme.configurationdata.ConfigurationDataBuilder;
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

    private JDA jda;

    public FusionDiscord(@NotNull final ComponentLogger logger, @NotNull final Path dataPath) {
        super(logger, dataPath);
    }

    public FusionDiscord start() {
        build(ConfigurationDataBuilder.createConfiguration(ConfigKeys.class, DiscordKeys.class));

        final String token = this.config.getProperty(DiscordKeys.token);

        final JDA jda = JDABuilder.createDefault(token).build();

        try {
            jda.awaitReady();
        } catch (final InterruptedException exception) {
            getLogger().error("Interrupted while waiting for JDA to start", exception);
        }

        this.jda = jda;

        return this;
    }

    public final Optional<JDA> getJda() {
        return Optional.ofNullable(this.jda);
    }
}