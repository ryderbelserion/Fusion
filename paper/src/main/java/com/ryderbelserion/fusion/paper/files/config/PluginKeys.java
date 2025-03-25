package com.ryderbelserion.fusion.paper.files.config;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class PluginKeys implements SettingsHolder {

    @Comment({
            "A list of available custom item plugins",
            "  -> ItemsAdder",
            "  -> Oraxen",
            "  -> Nexo",
            "  -> None",
            "",
            "If the option is set to blank, it'll pick whatever plugin it feels like picking.",
            "Set the value to None if you do not want any."
    })
    public static final Property<String> items_plugin = newProperty("settings.custom-items-plugin", "None");

}