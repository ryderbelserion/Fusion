package com.ryderbelserion.fusion.core;

import com.ryderbelserion.fusion.core.api.support.objects.FusionKey;
import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import org.jetbrains.annotations.NotNull;

public class FusionConfig {

    public static final FusionKey fusion_config = FusionKey.key("fusion-config", "fusion.yml");
    public static final FusionKey fusion_cache = FusionKey.key("fusion-cache", "fusion-cache.json");

    private final YamlCustomFile config;

    public FusionConfig(@NotNull final YamlCustomFile config) {
        this.config = config;
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