package com.ryderbelserion.fusion.kyori.registry.mods.objects;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.api.FusionKey;
import com.ryderbelserion.fusion.core.api.FusionProvider;
import com.ryderbelserion.fusion.kyori.registry.mods.interfaces.AbstractMod;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Mod extends AbstractMod {

    private final FusionCore fusion = FusionProvider.getInstance();

    public Mod(final FusionKey key) {
        super(key);
    }

    @Override
    public boolean isEnabled() {
        return this.fusion.isModReady(getKey());
    }
}