package com.ryderbelserion.fusion.kyori.mods.interfaces;

import com.ryderbelserion.fusion.core.FusionKey;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class IMod {

    public abstract void setKey(@NotNull final FusionKey key);

    public abstract @NotNull FusionKey key();

    public abstract boolean isEnabled();

    public boolean isIgnored(@NotNull final UUID uuid, @NotNull final UUID target) {
        return false;
    }

    public IMod accept(@NotNull final Consumer<IMod> consumer) {
        consumer.accept(this);

        return this;
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
}