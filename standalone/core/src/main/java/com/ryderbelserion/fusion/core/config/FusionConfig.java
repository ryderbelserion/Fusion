package com.ryderbelserion.fusion.core.config;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import org.jetbrains.annotations.NotNull;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class FusionConfig implements SettingsHolder {

    @Override
    public void registerComments(@NotNull CommentsConfiguration conf) {
        conf.setComment("settings", "Configure the library bundled with the plugin.");
    }

    @Comment({
            "This option defines if the plugin/library logs everything,",
            "Very useful if you have an issue with the plugin!"
    })
    public static final Property<Boolean> is_verbose = newProperty("settings.is_verbose", true);

    @Comment("This controls the format of the numerical data.")
    public static final Property<String> number_format = newProperty("settings.number_format", "#,###.##");

    @Comment({
            "This controls the type of rounding for how the numerical data is rounded.",
            "",
            "Available types: up, down, ceiling, floor, half_up, half_down, half_even, unnecessary"
    })
    public static final Property<String> rounding_format = newProperty("settings.rounding", "half_even");

    @Comment({
            "This config option decides how deep to search through a given folder.",
            "It only applies to folders that are dynamic like the crates folder or vouchers."
    })
    public static final Property<Integer> recursion_depth = newProperty("settings.recursion_depth", 1);

    @Comment({
            "A list of available custom item plugins",
            " -> ItemsAdder",
            " -> Oraxen",
            " -> Nexo",
            " -> None",
            "",
            "If the option is set to blank, it'll pick whatever plugin it feels like picking.",
            "Set the value to None if you do not want any."
    })
    public static final Property<String> custom_items_plugin = newProperty("settings.custom-items-plugin", "None");

}