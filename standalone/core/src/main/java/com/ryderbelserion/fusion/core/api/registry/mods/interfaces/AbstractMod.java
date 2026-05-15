package com.ryderbelserion.fusion.core.api.registry.mods.interfaces;

import com.ryderbelserion.fusion.core.api.FusionKey;
import org.jspecify.annotations.NonNull;
import java.util.UUID;

public abstract class AbstractMod {

    private final FusionKey key;

    public AbstractMod(@NonNull final FusionKey key) {
        this.key = key;
    }

    public abstract boolean isEnabled();

    public AbstractMod enable() {
        return this;
    }

    public AbstractMod disable() {
        return this;
    }

    public boolean isIgnored(@NonNull final UUID uuid, @NonNull final UUID target) {
        return false;
    }

    public boolean isVanished(@NonNull final UUID uuid) {
        return false;
    }

    public boolean isMuted(@NonNull final UUID uuid) {
        return false;
    }

    public boolean isAfk(@NonNull final UUID uuid) {
        return false;
    }

    public @NonNull final FusionKey getKey() {
        return this.key;
    }
}