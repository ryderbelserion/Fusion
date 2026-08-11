package com.ryderbelserion.fusion.kyori.registry.mods.interfaces;

import com.ryderbelserion.fusion.api.objects.FusionKey;
import org.jspecify.annotations.NullMarked;
import java.util.Map;

@NullMarked
public interface AbstractRegistry {

    void addMod(final FusionKey key, final AbstractMod mod);

    void removeMod(final FusionKey key);

    Map<FusionKey, AbstractMod> getMods();

    AbstractMod getMod(final FusionKey key);

}