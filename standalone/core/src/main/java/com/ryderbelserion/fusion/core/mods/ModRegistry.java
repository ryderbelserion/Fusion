package com.ryderbelserion.fusion.core.mods;

import com.ryderbelserion.fusion.api.enums.constants.ModSupport;
import com.ryderbelserion.fusion.api.objects.FusionKey;
import com.ryderbelserion.fusion.api.exceptions.FusionException;
import com.ryderbelserion.fusion.api.interfaces.IModRegistry;
import com.ryderbelserion.fusion.core.mods.objects.Mod;
import org.jspecify.annotations.NullMarked;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@NullMarked
public final class ModRegistry implements IModRegistry<Mod> {

    private final Map<FusionKey, Mod> mods = new HashMap<>();

    public void init() {
        ModSupport.dependencies.forEach(dependency -> addMod(dependency, new Mod(dependency)));
    }

    @Override
    public void addMod(final FusionKey key, final Mod mod) {
        mod.init();

        this.mods.putIfAbsent(key, mod);
    }

    @Override
    public void removeMod(final FusionKey key) {
        if (!this.mods.containsKey(key) || ModSupport.dependencies.contains(key)) return;

        this.mods.remove(key).stop();
    }

    @Override
    public Map<FusionKey, Mod> getMods() {
        return Collections.unmodifiableMap(this.mods);
    }

    @Override
    public Mod getMod(final FusionKey key) {
        if (!this.mods.containsKey(key)) {
            throw new FusionException("Could not find mod for key " + key);
        }

        return this.mods.get(key);
    }
}