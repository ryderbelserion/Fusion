package com.ryderbelserion.fusion.core.api.registry.mods.objects;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.core.api.registry.mods.interfaces.AbstractMod;
import org.jetbrains.annotations.NotNull;

public class Mod extends AbstractMod {

    private final FusionCore fusion = FusionProvider.getInstance();

    public Mod(@NotNull final FusionKey key) {
        super(key);
    }

    @Override
    public boolean isEnabled() {
        return this.fusion.isModReady(getKey());
    }
}