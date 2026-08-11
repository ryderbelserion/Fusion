package com.ryderbelserion.fusion.api;

import com.ryderbelserion.fusion.api.exceptions.FusionException;

public class FusionProvider {

    private static FusionApi fusion;

    public static FusionApi api() {
        if (FusionProvider.fusion == null) {
            throw new FusionException("Failed to fetch Fusion API, is it enabled?");
        }

        return FusionProvider.fusion;
    }

    public static void register(final FusionApi fusion) {
        FusionProvider.fusion = fusion;
    }

    public static void unregister() {
        FusionProvider.fusion = null;
    }

    public static boolean isReady() {
        return FusionProvider.fusion != null;
    }
}