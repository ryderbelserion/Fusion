package com.ryderbelserion.fusion.config;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class ConfigKeys implements SettingsHolder {

    public static final Property<String> root_key = newProperty("root-key", "this is a value");

}