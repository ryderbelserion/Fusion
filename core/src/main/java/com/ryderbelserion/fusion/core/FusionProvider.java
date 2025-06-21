package com.ryderbelserion.fusion.core;

import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public class FusionProvider {

    private static @Nullable FusionCore core;

    @ApiStatus.Internal
    public static void register(@NotNull final FusionCore core) {
        FusionProvider.core = core;
    }

    @ApiStatus.Internal
    public static void unregister() {
        FusionProvider.core = null;
    }

    @ApiStatus.Internal
    public static @NotNull FusionCore get() {
        if (core == null) {
            throw new FusionException("FusionCore is not initialized.");
        }

        return core;
    }
}