package com.ryderbelserion.fusion.core.api.addons.objects;

import org.jetbrains.annotations.NotNull;
import java.util.Properties;

/**
 * This holds information relating to addons that are created, It pulls information from property files.
 */
public class Addon {

    private final String main;
    private final String name;

    /**
     * Builds an addon object.
     *
     * @param properties the properties to pull information from
     */
    public Addon(@NotNull final Properties properties) {
        this.main = properties.getProperty("main", "N/A");
        this.name = properties.getProperty("name", "N/A");
    }

    /**
     * Gets the addon domain i.e. com.ryderbelserion which is the class path.
     *
     * @return the addon domain
     */
    public @NotNull String getMain() {
        return this.main;
    }

    /**
     * Gets the name of the addon i.e. beans.
     *
     * @return the name of the addon
     */
    public @NotNull String getName() {
        return this.name;
    }
}