package com.ryderbelserion.fusion.core.api;

import com.ryderbelserion.fusion.core.api.interfaces.IFusionKey;
import org.jspecify.annotations.NonNull;

public class FusionKey extends IFusionKey {

    private final String namespace;
    private final String value;

    public FusionKey(@NonNull final String namespace, @NonNull final String value) {
        this.namespace = namespace;

        this.value = value;
    }

    public static FusionKey key(@NonNull final String namespace, @NonNull final String value) {
        return new FusionKey(namespace, value);
    }

    public static FusionKey key(@NonNull final String value) {
        if (!value.contains(":")) return key(getFusionNamespace(), value);

        final String[] split = value.split(":");

        return new FusionKey(split[0], split[1]);
    }

    public static String getFusionNamespace() {
        return "fusion";
    }

    @Override
    public @NonNull String getNamespace() {
        return this.namespace;
    }

    @Override
    public @NonNull String getValue() {
        return this.value;
    }

    @Override
    public @NonNull String asString() {
        return this.namespace + ":" + this.value;
    }
}