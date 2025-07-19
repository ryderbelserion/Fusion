package com.ryderbelserion.fusion.core.api.support.objects;

import com.ryderbelserion.fusion.core.api.interfaces.mods.IMod;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class Mod extends IMod {

    private Key key;

    @Override
    public void setKey(@NotNull final Key key) {
        this.key = key;
    }

    @Override
    public boolean isEnabled() {
        return this.fusion.isModReady(this.key);
    }

    @Override
    public @NotNull final Key key() {
        return this.key;
    }
}