package com.ryderbelserion.fusion.paper;

import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FusionProvider {

    private static @Nullable FusionPaper instance;

    @ApiStatus.Internal
    public static void register(@NotNull final FusionPaper instance) {
        FusionProvider.instance = instance;
    }

    @ApiStatus.Internal
    public static void unregister() {
        FusionProvider.instance = null;
    }

    public static @NotNull FusionPaper getInstance() {
        if (instance == null) throw new FusionException("Fusion API is not yet initialized.");

        return instance;
    }
}