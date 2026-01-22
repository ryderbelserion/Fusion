package com.ryderbelserion.fusion.core.api.objects;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.core.api.interfaces.IMod;
import org.jetbrains.annotations.NotNull;

public class Mod extends IMod {

    private final FusionCore fusion = FusionProvider.getInstance();

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