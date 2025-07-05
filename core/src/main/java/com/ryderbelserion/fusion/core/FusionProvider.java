package com.ryderbelserion.fusion.core;

import com.ryderbelserion.fusion.core.api.FusionCore;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FusionProvider {

    private static @Nullable FusionCore api;

    @ApiStatus.Internal
    public static void register(@NotNull final FusionCore api) {
        FusionProvider.api = api;
    }

    @ApiStatus.Internal
    public static void unregister() {
        FusionProvider.api = null;
    }

    @ApiStatus.Internal
    public static @NotNull FusionCore get() {
        if (api == null) {
            throw new FusionException("Fusion API is not yet initialized.");
        }

        return api;
    }
}