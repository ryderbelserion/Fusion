package com.ryderbelserion.fusion.core.api.interfaces;

import com.ryderbelserion.fusion.core.api.FusionKey;
import org.jetbrains.annotations.NotNull;
import java.util.Map;

public interface IModRegistry {

    void addMod(@NotNull final FusionKey key, @NotNull final IMod mod);

    void removeMod(@NotNull final FusionKey key);

    Map<FusionKey, IMod> getMods();

    @NotNull IMod getMod(@NotNull final FusionKey key);

}