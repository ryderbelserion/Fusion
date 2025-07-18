package com.ryderbelserion.fusion.core.api.configuration.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.Set;

public interface IConfigurationSection {

    @NotNull String getEffectivePath();

    @NotNull String getPath();

    @NotNull Set<String> getKeys(boolean deep);

    @NotNull Map<String, Object> getValues(boolean deep);

    void set(@NotNull String path, @Nullable Object value);

    @Nullable Object get(@NotNull String path);

    boolean isConfigurationSection(@NotNull String path);

    @Nullable IConfigurationSection getConfigurationSection(@NotNull String path);

    @Nullable IConfigurationSection getDefaultSection();

    @Nullable IConfigurationSection getParent();

    @Nullable IConfiguration getRoot();

}