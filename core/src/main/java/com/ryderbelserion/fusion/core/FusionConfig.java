package com.ryderbelserion.fusion.core;

import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public class FusionConfig {

    private final YamlCustomFile config;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public FusionConfig(@NotNull final Optional<YamlCustomFile> config) {
        this.config = config.orElseThrow();
    }

    public void reload() {
        this.config.load();
    }

    public String getNumberFormat() {
        return this.config.getStringValueWithDefault("#,###.##", "settings", "number_format");
    }

    public String getItemsPlugin() {
        return this.config.getStringValueWithDefault("None", "settings", "custom-items-plugin");
    }

    public String getRoundingFormat() {
        return this.config.getStringValueWithDefault("half_even", "settings", "rounding");
    }

    public boolean isVerbose() {
        return this.config.getBooleanValueWithDefault(false, "settings", "is_verbose");
    }

    public int getDepth() {
        return this.config.getIntValueWithDefault(1, "settings", "recursion_depth");
    }
}