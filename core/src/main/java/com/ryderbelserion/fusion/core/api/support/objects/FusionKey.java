package com.ryderbelserion.fusion.core.api.support.objects;

import com.ryderbelserion.fusion.core.api.interfaces.IFusionKey;
import org.jetbrains.annotations.NotNull;

public class FusionKey extends IFusionKey {

    private final String namespace;
    private final String value;

    public FusionKey(@NotNull final String namespace, @NotNull final String value) {
        this.namespace = namespace;

        this.value = value;
    }

    @Override
    public @NotNull String getNamespace() {
        return this.namespace;
    }

    @Override
    public @NotNull String getValue() {
        return this.value;
    }

    @Override
    public @NotNull String asString() {
        return this.namespace + ":" + this.value;
    }

    public static FusionKey key(@NotNull final String namespace, @NotNull final String value) {
        return new FusionKey(namespace, value);
    }

    public static FusionKey key(@NotNull final String value) {
        if (!value.contains(":")) return key(getFusionNamespace(), value);

        final String[] split = value.split(":");

        return new FusionKey(split[0], split[1]);
    }

    public static String getFusionNamespace() {
        return "fusion";
    }
}