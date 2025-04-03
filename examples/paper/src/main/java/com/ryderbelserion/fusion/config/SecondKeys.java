package com.ryderbelserion.fusion.config;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class SecondKeys implements SettingsHolder {

    public static final Property<String> second_key = newProperty("second-key", "this is another value");

}