package com.ryderbelserion.fusion.discord;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import org.jetbrains.annotations.NotNull;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class DiscordKeys implements SettingsHolder {

    @Override
    public void registerComments(@NotNull final CommentsConfiguration configuration) {
        configuration.setComment("discord", "Config options related to Discord Bots!");

        configuration.setComment("discord.bot", "Directly configure the Discord Bot!");
    }

    @Comment("The bot token which is required to start the discord bot.")
    public static final Property<String> token = newProperty("discord.bot.token", "your_token");

}