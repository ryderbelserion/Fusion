package com.ryderbelserion.fusion.core.api.objects;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.core.api.interfaces.IMod;
import org.jetbrains.annotations.NotNull;

public class Mod extends IMod {

    private final FusionCore fusion = FusionProvider.getInstance();

    public Mod(@NotNull final FusionKey key) {
        super(key);
    }

    @Override
    public boolean isEnabled() {
        return this.fusion.isModReady(getKey());
    }
}