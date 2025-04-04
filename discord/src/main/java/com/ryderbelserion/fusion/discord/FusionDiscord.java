package com.ryderbelserion.fusion.discord;

import ch.qos.logback.classic.Logger;
import com.ryderbelserion.fusion.api.FusionApi;
import com.ryderbelserion.fusion.api.configs.keys.ConfigKeys;
import com.ryderbelserion.fusion.discord.configs.DiscordKeys;
import com.ryderbelserion.fusion.discord.managers.LoggerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.Optional;

public class FusionDiscord extends FusionApi {

    private final LoggerManager logger;
    private final Path dataFolder;

    public FusionDiscord(@NotNull final Path dataFolder, @NotNull final Logger logger) {
        this.logger = new LoggerManager(logger);
        this.dataFolder = dataFolder;

        build();
    }

    private Optional<JDA> jda;

    public void build() {
        Provider.register(this);

        init(ConfigKeys.class, DiscordKeys.class);

        final String token = this.config.getProperty(DiscordKeys.token);

        final JDA jda = JDABuilder.createDefault(token).build();

        try {
            jda.awaitReady();
        } catch (final InterruptedException exception) {
            exception.printStackTrace();
        }

        this.jda = Optional.of(jda);
    }

    public final Optional<JDA> getJda() {
        return this.jda;
    }

    @Override
    public @NotNull final LoggerManager getLogger() {
        return this.logger;
    }

    @Override
    public @NotNull final Path getDataFolder() {
        return this.dataFolder;
    }
}