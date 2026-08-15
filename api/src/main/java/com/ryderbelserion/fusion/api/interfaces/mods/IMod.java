package com.ryderbelserion.fusion.api.interfaces.mods;

import com.ryderbelserion.fusion.api.objects.FusionKey;
import org.jspecify.annotations.NullMarked;
import java.util.UUID;

@NullMarked
public abstract class IMod {

    private final FusionKey key;

    public IMod(final FusionKey key) {
        this.key = key;
    }

    public abstract boolean isEnabled();

    public void init() {

    }

    public void stop() {

    }

    public boolean isIgnored(final UUID uuid, final UUID target) {
        return false;
    }

    public boolean isVanished(final UUID uuid) {
        return false;
    }

    public boolean isMuted(final UUID uuid) {
        return false;
    }

    public boolean isAfk(final UUID uuid) {
        return false;
    }

    public final FusionKey getKey() {
        return this.key;
    }
}