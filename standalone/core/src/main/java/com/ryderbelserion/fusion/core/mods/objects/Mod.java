package com.ryderbelserion.fusion.core.mods.objects;

import com.ryderbelserion.fusion.api.FusionProvider;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.api.objects.FusionKey;
import com.ryderbelserion.fusion.api.interfaces.mods.IMod;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Mod extends IMod {

    private final FusionCore fusion = (FusionCore) FusionProvider.api();

    public Mod(final FusionKey key) {
        super(key);
    }

    @Override
    public boolean isEnabled() {
        return this.fusion.isModReady(getKey());
    }
}