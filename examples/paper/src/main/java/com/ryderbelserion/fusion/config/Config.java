package com.ryderbelserion.fusion.config;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class Config implements SettingsHolder {

    public static final Property<String> prefix = newProperty("prefix", "<red>Fusion");

}