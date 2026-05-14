package com.ryderbelserion.fusion.core.api.registry.mods.interfaces;

import com.ryderbelserion.fusion.core.api.FusionKey;
import org.jetbrains.annotations.NotNull;
import java.util.Map;

public interface AbstractRegistry {

    void addMod(@NotNull final FusionKey key, @NotNull final AbstractMod mod);

    void removeMod(@NotNull final FusionKey key);

    Map<FusionKey, AbstractMod> getMods();

    @NotNull AbstractMod getMod(@NotNull final FusionKey key);

}