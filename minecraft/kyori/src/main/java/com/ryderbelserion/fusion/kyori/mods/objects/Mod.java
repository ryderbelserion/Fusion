package com.ryderbelserion.fusion.kyori.mods.objects;

import com.ryderbelserion.fusion.core.FusionKey;
import com.ryderbelserion.fusion.kyori.mods.interfaces.IMod;
import org.jetbrains.annotations.NotNull;

public class Mod extends IMod {

    private FusionKey key;

    @Override
    public void setKey(@NotNull final FusionKey key) {
        this.key = key;
    }

    @Override
    public @NotNull final FusionKey key() {
        return this.key;
    }

    @Override
    public boolean isEnabled() {
        return this.fusion.isModReady(this.key);
    }
}