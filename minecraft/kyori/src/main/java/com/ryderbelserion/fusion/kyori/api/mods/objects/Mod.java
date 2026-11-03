package com.ryderbelserion.fusion.kyori.api.mods.objects;

import com.ryderbelserion.fusion.api.FusionProvider;
import com.ryderbelserion.fusion.api.objects.FusionKey;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.kyori.api.mods.interfaces.AbstractMod;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Mod extends AbstractMod {

    private final FusionKyori fusion = (FusionKyori) FusionProvider.api();

    public Mod(final FusionKey key) {
        super(key);
    }

    @Override
    public boolean isEnabled() {
        return this.fusion.isModReady(getKey());
    }
}