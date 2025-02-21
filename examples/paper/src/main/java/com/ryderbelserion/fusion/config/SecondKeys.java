package com.ryderbelserion.fusion.config;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import com.ryderbelserion.fusion.config.beans.EntryProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newBeanProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class SecondKeys implements SettingsHolder {

    @Comment("This is the message for the /lobby help command!")
    public static final Property<EntryProperty> help = newBeanProperty(EntryProperty.class, "root.help", new EntryProperty().populate());

    public static final Property<String> second_key = newProperty("second-key", "this is another value");

}