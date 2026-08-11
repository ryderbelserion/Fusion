package com.ryderbelserion.fusion.api.objects;

import com.ryderbelserion.fusion.api.interfaces.AbstractFusionKey;
import org.jspecify.annotations.NullMarked;
import java.util.Objects;

@NullMarked
public class FusionKey extends AbstractFusionKey {

    private final String namespace;
    private final String value;

    public FusionKey(final String namespace, final String value) {
        this.namespace = namespace;

        this.value = value;
    }

    public static FusionKey key(final String namespace, final String value) {
        return new FusionKey(namespace, value);
    }

    @Override
    public String getNamespace() {
        return this.namespace;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String asString() {
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