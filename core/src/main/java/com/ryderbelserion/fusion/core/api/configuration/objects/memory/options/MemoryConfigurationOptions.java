package com.ryderbelserion.fusion.core.api.configuration.objects.memory.options;

import com.ryderbelserion.fusion.core.api.configuration.objects.options.ConfigurationOptions;
import com.ryderbelserion.fusion.core.api.configuration.objects.memory.MemoryConfiguration;
import org.jetbrains.annotations.NotNull;

public class MemoryConfigurationOptions extends ConfigurationOptions {

    public MemoryConfigurationOptions(@NotNull final MemoryConfiguration configuration) {
        super(configuration);
    }

    @Override
    public @NotNull MemoryConfiguration configuration() {
        return (MemoryConfiguration) super.configuration();
    }

    @Override
    public @NotNull MemoryConfigurationOptions copyDefaults(boolean copyDefaults) {
        super.copyDefaults(copyDefaults);

        return this;
    }

    @Override
    public @NotNull MemoryConfigurationOptions pathSeparator(char pathSeparator) {
        super.pathSeparator(pathSeparator);

        return this;
    }
}