package com.ryderbelserion.fusion.core.api.configuration.objects.memory;

import com.ryderbelserion.fusion.core.api.configuration.interfaces.IConfiguration;
import com.ryderbelserion.fusion.core.api.configuration.interfaces.IConfigurationSection;
import com.ryderbelserion.fusion.core.api.configuration.objects.memory.options.MemoryConfigurationOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;

public class MemoryConfiguration extends MemorySection implements IConfiguration {

    private MemoryConfigurationOptions options;
    private IConfiguration defaults;

    public MemoryConfiguration(@NotNull final IConfiguration defaults) {
        this.defaults = defaults;
    }

    public MemoryConfiguration() {}

    @Override
    public void addDefault(@NotNull String path, @Nullable Object value) {
        if (this.defaults == null) {
            this.defaults = new MemoryConfiguration();
        }

        this.defaults.set(path, value);
    }

    @Override
    public void addDefaults(@NotNull final Map<String, Object> defaults) {
        for (final Map.Entry<String, Object> entry : defaults.entrySet()) {
            this.addDefault(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void addDefaults(@NotNull final IConfiguration defaults) {
        for (final String key : defaults.getKeys(true)) {
            if (defaults.isConfigurationSection(key)) continue;

            this.addDefault(key, defaults.get(key));
        }
    }

    @Override
    public void setDefaults(@NotNull final IConfiguration defaults) {
        this.defaults = defaults;
    }

    @Override
    public IConfiguration defaults() {
        return this.defaults;
    }

    @Override
    public @Nullable IConfigurationSection getParent() {
        return null;
    }

    @Override
    public MemoryConfigurationOptions options() {
        if (this.options == null) {
            this.options = new MemoryConfigurationOptions(this);
        }

        return this.options;
    }
}