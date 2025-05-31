package com.ryderbelserion.fusion.core.api.addons.objects;

import org.jetbrains.annotations.NotNull;
import java.util.Properties;

public class Addon {

    private final String main;
    private final String name;

    public Addon(@NotNull final Properties properties) {
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