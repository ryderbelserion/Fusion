package com.ryderbelserion.fusion.core.api.registry;

import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.interfaces.IMod;
import com.ryderbelserion.fusion.core.api.constants.ModSupport;
import com.ryderbelserion.fusion.core.api.objects.Mod;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModRegistry {

    public Map<FusionKey, IMod> mods = new HashMap<>();

    public void init() {
        ModSupport.dependencies.forEach(dependency -> addMod(dependency, new Mod()));
    }

    public void addMod(@NotNull final FusionKey key, @NotNull final IMod mod) {
        this.mods.putIfAbsent(key, mod.accept(consumer -> consumer.setKey(key)).start());
    }

    public void removeMod(@NotNull final FusionKey key) {
        if (!this.mods.containsKey(key) || ModSupport.dependencies.contains(key)) return;

        this.mods.remove(key).stop();
    }

    public @NotNull final Map<FusionKey, IMod> getMods() {
        return Collections.unmodifiableMap(this.mods);
    }

    public IMod getMod(@NotNull final FusionKey key) {
        return this.mods.get(key);
    }
}