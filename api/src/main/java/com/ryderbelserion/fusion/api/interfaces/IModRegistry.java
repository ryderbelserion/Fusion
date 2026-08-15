package com.ryderbelserion.fusion.api.interfaces;

import com.ryderbelserion.fusion.api.interfaces.mods.IMod;
import com.ryderbelserion.fusion.api.objects.FusionKey;
import org.jspecify.annotations.NullMarked;
import java.util.Map;

@NullMarked
public interface IModRegistry<M extends IMod> {

    void addMod(final FusionKey key, final M mod);

    void removeMod(final FusionKey key);

    Map<FusionKey, M> getMods();

    M getMod(final FusionKey key);

}