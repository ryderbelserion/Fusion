package com.ryderbelserion.fusion.api.configs.keys;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class ConfigKeys implements SettingsHolder {

    @Override
    public void registerComments(final CommentsConfiguration comments) {
        comments.setComment("settings", "Configure the library bundled with the plugin.");
    }

    @Comment({
            "This option defines if the plugin/library logs everything,",
            "Very useful if you have an issue with the plugin!"
    })
    public static final Property<Boolean> is_verbose = newProperty("settings.is_verbose", false);

    @Comment("This controls the format of the numerical data.")
    public static final Property<String> number_format = newProperty("settings.number_format", "#,###.##");

    @Comment({
            "This controls the type of rounding for how the numerical data is rounded.",
            "",
            "Available types: up, down, ceiling, floor, half_up, half_down, half_even, unnecessary"
    })
    public static final Property<String> rounding = newProperty("settings.rounding", "half_even");

}