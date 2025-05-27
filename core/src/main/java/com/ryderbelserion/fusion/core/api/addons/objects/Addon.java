package com.ryderbelserion.fusion.core.api.addons.objects;

import java.util.Properties;

public class Addon {

    private final String main;
    private final String name;

    public Addon(final Properties properties) {
        this.main = properties.getProperty("main", "N/A");
        this.name = properties.getProperty("name", "N/A");
    }

    public String getMain() {
        return this.main;
    }

    public String getName() {
        return this.name;
    }
}