package com.ryderbelserion.fusion.core.api.interfaces;

import com.ryderbelserion.fusion.core.api.FusionKey;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public abstract class IMod {

    private final FusionKey key;

    public IMod(@NotNull final FusionKey key) {
        this.key = key;
    }

    public abstract boolean isEnabled();

    public IMod enable() {
        return this;
    }

    public IMod disable() {
        return this;
    }

    public boolean isIgnored(@NotNull final UUID uuid, @NotNull final UUID target) {
        return false;
    }

    public boolean isVanished(@NotNull final UUID uuid) {
        return false;
    }

    public boolean isMuted(@NotNull final UUID uuid) {
        return false;
    }

    public boolean isAfk(@NotNull final UUID uuid) {
        return false;
    }

    public @NotNull final FusionKey getKey() {
        return this.key;
    }
}