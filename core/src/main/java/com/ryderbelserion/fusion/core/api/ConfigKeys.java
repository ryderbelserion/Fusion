package com.ryderbelserion.fusion.core.api;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class ConfigKeys implements SettingsHolder {

    @Override
    public void registerComments(CommentsConfiguration conf) {
        conf.setComment("settings", "Configure the library bundled with the plugin.");
    }

    @Comment({
            "This option defines if the plugin/library logs everything,",
            "Very useful if you have an issue with the plugin!"
    })
    public static final Property<Boolean> is_verbose = newProperty("settings.is_verbose", false);

    @Comment({
            "This enables an experimental addon system which will create an addons folder",
            "The addons folder only accepts .jar or .zip files that contain addon.properties",
            "",
            "This option does work, and you can make addons however, Please do not use turn this to true",
            "I am not ready yet to write official documentation on how to use this!"
    })
    public static final Property<Boolean> addon_system = newProperty("settings.addon_system", false);

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
            "It only applies to folders that are dynamic like the crates folder or vouchers"
    })
    public static final Property<Integer> recursion_depth = newProperty("settings.recursion_depth", 1);

}