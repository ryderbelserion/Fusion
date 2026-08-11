package com.ryderbelserion.fusion.kyori.registry.mods.interfaces;

import com.ryderbelserion.fusion.api.objects.FusionKey;
import org.jspecify.annotations.NullMarked;
import java.util.UUID;

@NullMarked
public abstract class AbstractMod {

    private final FusionKey key;

    public AbstractMod(final FusionKey key) {
        this.key = key;
    }

    public abstract boolean isEnabled();

    public AbstractMod init() {
        return this;
    }

    public AbstractMod stop() {
        return this;
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