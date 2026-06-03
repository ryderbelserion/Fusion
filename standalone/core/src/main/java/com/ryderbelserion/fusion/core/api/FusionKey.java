package com.ryderbelserion.fusion.core.api;

import com.ryderbelserion.fusion.core.api.interfaces.IFusionKey;
import org.jspecify.annotations.NonNull;
import java.util.Objects;

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

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;

        if (!(other instanceof FusionKey fusionKey)) return false;

        return Objects.equals(this.namespace, fusionKey.getNamespace()) && Objects.equals(this.value, fusionKey.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.namespace, this.value);
    }
}