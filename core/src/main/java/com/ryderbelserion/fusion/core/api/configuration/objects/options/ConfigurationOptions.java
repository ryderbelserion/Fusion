package com.ryderbelserion.fusion.core.api.configuration.objects.options;

import com.ryderbelserion.fusion.core.api.configuration.interfaces.IConfiguration;
import org.jetbrains.annotations.NotNull;

public class ConfigurationOptions {

    private final IConfiguration configuration;
    private boolean copyDefaults = false;
    private char pathSeparator = '.';

    public ConfigurationOptions(@NotNull final IConfiguration configuration) {
        this.configuration = configuration;
    }

    @NotNull
    public IConfiguration configuration() {
        return this.configuration;
    }

    public char pathSeparator() {
        return this.pathSeparator;
    }

    @NotNull
    public ConfigurationOptions pathSeparator(final char pathSeparator) {
        this.pathSeparator = pathSeparator;

        return this;
    }

    public boolean copyDefaults() {
        return this.copyDefaults;
    }

    @NotNull
    public ConfigurationOptions copyDefaults(final boolean copyDefaults) {
        this.copyDefaults = copyDefaults;

        return this;
    }
}