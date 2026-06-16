package com.ryderbelserion.fusion.core.api.registry.mods;

import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.constants.ModSupport;
import com.ryderbelserion.fusion.core.api.registry.mods.interfaces.AbstractMod;
import com.ryderbelserion.fusion.core.api.registry.mods.interfaces.AbstractRegistry;
import com.ryderbelserion.fusion.core.api.registry.mods.objects.Mod;
import org.jspecify.annotations.NonNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModRegistry implements AbstractRegistry {

    public Map<FusionKey, AbstractMod> mods = new HashMap<>();

    public void init() {
        ModSupport.dependencies.forEach(dependency -> addMod(dependency, new Mod(dependency)));
    }

    @Override
    public void addMod(@NonNull final FusionKey key, @NonNull final AbstractMod mod) {
        this.mods.putIfAbsent(key, mod.init());
    }

    @Override
    public void removeMod(@NonNull final FusionKey key) {
        if (!this.mods.containsKey(key) || ModSupport.dependencies.contains(key)) return;

        this.mods.remove(key).stop();
    }

    @Override
    public @NonNull final Map<FusionKey, AbstractMod> getMods() {
        return Collections.unmodifiableMap(this.mods);
    }

    @Override
    public @NonNull AbstractMod getMod(@NonNull final FusionKey key) {
        if (!this.mods.containsKey(key)) {
            throw new FusionException("Could not find mod for key " + key);
        }

        return this.mods.get(key);
    }
}