package com.ryderbelserion.fusion.core.api.support.objects;

import com.ryderbelserion.fusion.core.api.interfaces.mods.IMod;
import org.jetbrains.annotations.NotNull;

public class Mod extends IMod {

    private FusionKey key;

    @Override
    public void setKey(@NotNull final FusionKey key) {
        this.key = key;
    }

    @Override
    public boolean isEnabled() {
        return this.fusion.isModReady(this.key);
    }

    @Override
    public @NotNull final FusionKey key() {
        return this.key;
    }
}