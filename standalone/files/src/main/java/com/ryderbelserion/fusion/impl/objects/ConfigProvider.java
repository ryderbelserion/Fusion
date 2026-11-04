package com.ryderbelserion.fusion.impl.objects;

import com.ryderbelserion.fusion.files.exceptions.CustomException;
import com.ryderbelserion.fusion.impl.FusionConfig;

public class ConfigProvider {

    private static FusionConfig fusion;

    public static FusionConfig api() {
        if (!isReady()) {
            throw new CustomException("Failed to fetch Fusion Config, is it enabled?");
        }

        return ConfigProvider.fusion;
    }

    public static void register(final FusionConfig fusion) {
        ConfigProvider.fusion = fusion;
    }

    public static void unregister() {
        ConfigProvider.fusion = null;
    }

    public static boolean isReady() {
        return ConfigProvider.fusion != null;
    }
    
}