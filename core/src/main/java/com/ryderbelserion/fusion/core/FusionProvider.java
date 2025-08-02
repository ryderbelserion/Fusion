package com.ryderbelserion.fusion.core;

import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FusionProvider {

    private static @Nullable FusionCore instance;

    @ApiStatus.Internal
    public static void register(@NotNull final FusionCore instance) {
        FusionProvider.instance = instance;
    }

    @ApiStatus.Internal
    public static void unregister() {
        FusionProvider.instance = null;
    }

    public static @NotNull FusionCore getInstance() {
        if (instance == null) throw new FusionException("Fusion API is not yet initialized.");

        return instance;
    }
}