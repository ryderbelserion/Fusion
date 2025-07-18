package com.ryderbelserion.fusion.core.api.configuration.interfaces;

import com.ryderbelserion.fusion.core.api.configuration.objects.options.ConfigurationOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;

public interface IConfiguration extends IConfigurationSection {

    void addDefault(@NotNull final String path, @Nullable final Object value);

    void addDefaults(@NotNull final Map<String, Object> defaults);

    void addDefaults(@NotNull final IConfiguration defaults);

    void setDefaults(@NotNull final IConfiguration defaults);

    IConfiguration defaults();

    ConfigurationOptions options();

}