package com.ryderbelserion.fusion.core.api.registry.mods.interfaces;

import com.ryderbelserion.fusion.core.api.FusionKey;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public abstract class AbstractMod {

    private final FusionKey key;

    public AbstractMod(@NotNull final FusionKey key) {
        this.key = key;
    }

    public abstract boolean isEnabled();

    public AbstractMod enable() {
        return this;
    }

    public AbstractMod disable() {
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