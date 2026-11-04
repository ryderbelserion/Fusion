package com.ryderbelserion.fusion.impl.keys;

import com.ryderbelserion.fusion.config.annotations.Comment;
import com.ryderbelserion.fusion.config.properties.builders.CommentsBuilder;
import com.ryderbelserion.fusion.config.properties.interfaces.IPropertyHolder;
import com.ryderbelserion.fusion.config.properties.objects.interfaces.Property;
import org.jspecify.annotations.NullMarked;
import static com.ryderbelserion.fusion.config.properties.PropertyBuilder.newProperty;

@NullMarked
public class RootKeys implements IPropertyHolder {

    @Override
    public void registerComments(CommentsBuilder builder) {
        builder.setComment("settings", "Configure the library bundled with the plugin.");
    }

    @Comment("This option defines if the plugin/library logs everything,")
    public static final Property<Boolean> is_verbose = newProperty(false, "settings", "is_verbose");

    @Comment("This controls the format of the numerical data.")
    public static final Property<String> number_format = newProperty("#,###.##", "settings", "number_format");

    @Comment({
            "This controls the type of rounding for how the numerical data is rounded.",
            "",
            "Available types: up, down, ceiling, floor, half_up, half_down, half_even, unnecessary"
    })
    public static final Property<String> rounding = newProperty("half_even", "settings", "rounding");

    @Comment({
            "This config option decides how deep to search through a given folder.",
            "It only applies to folders that are dynamic like the crates folder or vouchers."
    })
    public static final Property<Integer> recursion_depth = newProperty(1, "settings", "recursion_depth");

    @Comment({
            "A list of available custom item plugins",
            "  -> ItemsAdder",
            "   -> Oraxen",
            "   -> Nexo",
            "   -> None",
            "",
            "If the option is set to blank, it'll pick whatever plugin it feels like picking.",
            "Set the value to None if you do not want any."
    })
    public static final Property<String> custom_items_plugin = newProperty("None", "settings", "custom-items-plugin");
}