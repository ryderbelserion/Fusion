package com.ryderbelserion.keys;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.BooleanProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class ConfigKeys implements SettingsHolder {

    public static final BooleanProperty test = newProperty("test", true);

}