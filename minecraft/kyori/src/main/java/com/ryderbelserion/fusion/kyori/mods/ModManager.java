package com.ryderbelserion.fusion.kyori.mods;

import com.ryderbelserion.fusion.core.FusionKey;
import com.ryderbelserion.fusion.kyori.mods.interfaces.IMod;
import com.ryderbelserion.fusion.kyori.mods.objects.Mod;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModManager {

    public Map<FusionKey, IMod> mods = new HashMap<>();

    public void addMod(@NotNull final FusionKey key, @NotNull final Mod mod) {
        this.mods.putIfAbsent(key, mod.accept(consumer -> consumer.setKey(key)));
    }

    public void removeMod(@NotNull final FusionKey key) {
        if (!this.mods.containsKey(key) || ModSupport.dependencies.contains(key)) return;

        this.mods.remove(key);
    }

    public @NotNull final Map<FusionKey, IMod> getMods() {
        return Collections.unmodifiableMap(this.mods);
    }

    public IMod getMod(@NotNull final FusionKey key) {
        return this.mods.get(key);
    }
}