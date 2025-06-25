package com.ryderbelserion.fusion.common;

import com.ryderbelserion.fusion.common.api.FusionCommon;
import com.ryderbelserion.fusion.common.api.exceptions.FusionException;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FusionProvider {

    private static @Nullable FusionCommon api;

    @ApiStatus.Internal
    public static void register(@NotNull final FusionCommon api) {
        FusionProvider.api = api;
    }

    @ApiStatus.Internal
    public static void unregister() {
        FusionProvider.api = null;
    }

    @ApiStatus.Internal
    public static @NotNull FusionCommon get() {
        if (api == null) {
            throw new FusionException("Fusion API is not yet initialized.");
        }

        return api;
    }
}