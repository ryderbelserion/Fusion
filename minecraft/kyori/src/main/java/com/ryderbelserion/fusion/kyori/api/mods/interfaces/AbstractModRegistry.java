package com.ryderbelserion.fusion.kyori.api.mods.interfaces;

import com.ryderbelserion.fusion.api.objects.FusionKey;
import com.ryderbelserion.fusion.kyori.api.mods.objects.Mod;
import org.jspecify.annotations.NullMarked;
import java.util.Map;

@NullMarked
public interface AbstractModRegistry {

    void addMod(final FusionKey key, final Mod mod);

    void removeMod(final FusionKey key);

    Map<FusionKey, Mod> getMods();

    Mod getMod(final FusionKey key);

}