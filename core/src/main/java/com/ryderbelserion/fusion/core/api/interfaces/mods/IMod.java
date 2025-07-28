package com.ryderbelserion.fusion.core.api.interfaces.mods;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.support.objects.ModKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class IMod {

    @ApiStatus.Internal
    protected FusionCore fusion;

    public IMod accept(@NotNull final Consumer<IMod> consumer) {
        consumer.accept(this);

        return this;
    }

    @ApiStatus.Internal
    public void setFusion(@NotNull final FusionCore fusion) {
        this.fusion = fusion;
    }

    public abstract void setKey(@NotNull final ModKey key);

    public abstract boolean isEnabled();

    public abstract @NotNull ModKey key();

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
}