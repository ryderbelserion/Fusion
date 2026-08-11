package com.ryderbelserion.fusion.kyori.registry.mods;

import com.ryderbelserion.fusion.api.objects.FusionKey;
import com.ryderbelserion.fusion.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.constants.ModSupport;
import com.ryderbelserion.fusion.kyori.registry.mods.interfaces.AbstractMod;
import com.ryderbelserion.fusion.kyori.registry.mods.interfaces.AbstractRegistry;
import com.ryderbelserion.fusion.kyori.registry.mods.objects.Mod;
import org.jspecify.annotations.NullMarked;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@NullMarked
public final class ModRegistry implements AbstractRegistry {

    public Map<FusionKey, AbstractMod> mods = new HashMap<>();

    public void init() {
        ModSupport.dependencies.forEach(dependency -> addMod(dependency, new Mod(dependency)));
    }

    @Override
    public void addMod(final FusionKey key, final AbstractMod mod) {
        this.mods.putIfAbsent(key, mod.init());
    }

    @Override
    public void removeMod(final FusionKey key) {
        if (!this.mods.containsKey(key) || ModSupport.dependencies.contains(key)) return;

        this.mods.remove(key).stop();
    }

    @Override
    public Map<FusionKey, AbstractMod> getMods() {
        return Collections.unmodifiableMap(this.mods);
    }

    @Override
    public AbstractMod getMod(final FusionKey key) {
        if (!this.mods.containsKey(key)) {
            throw new FusionException("Could not find mod for key " + key);
        }

        return this.mods.get(key);
    }
}