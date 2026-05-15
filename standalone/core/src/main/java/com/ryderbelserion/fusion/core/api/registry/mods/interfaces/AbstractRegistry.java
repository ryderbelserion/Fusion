package com.ryderbelserion.fusion.core.api.registry.mods.interfaces;

import com.ryderbelserion.fusion.core.api.FusionKey;
import org.jspecify.annotations.NonNull;
import java.util.Map;

public interface AbstractRegistry {

    void addMod(@NonNull final FusionKey key, @NonNull final AbstractMod mod);

    void removeMod(@NonNull final FusionKey key);

    Map<FusionKey, AbstractMod> getMods();

    @NonNull AbstractMod getMod(@NonNull final FusionKey key);

}