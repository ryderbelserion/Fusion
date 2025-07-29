package com.ryderbelserion.fusion.core.api.support.objects;

import com.ryderbelserion.fusion.core.api.interfaces.mods.IModKey;
import org.jetbrains.annotations.NotNull;

public class ModKey extends IModKey {

    private final String namespace;
    private final String value;

    public ModKey(@NotNull final String namespace, @NotNull final String value) {
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

    public static ModKey key(@NotNull final String namespace, @NotNull final String value) {
        return new ModKey(namespace, value);
    }

    public static ModKey key(@NotNull final String namespace) {
        if (!namespace.contains(":")) return new ModKey(namespace, "");

        final String[] split = namespace.split(":");

        return new ModKey(split[0], split[1]);
    }
}