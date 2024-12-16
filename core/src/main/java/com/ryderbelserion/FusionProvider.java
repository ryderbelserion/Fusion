package com.ryderbelserion;

public class FusionProvider {

    private static FusionLayout layout = null;

    public static void register(final FusionLayout layout) {
        FusionProvider.layout = layout;
    }

    public static FusionLayout get() {
        return layout;
    }
}