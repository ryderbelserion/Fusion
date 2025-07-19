package com.ryderbelserion.fusion.core.api.support;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.interfaces.mods.IMod;
import com.ryderbelserion.fusion.core.api.support.objects.Mod;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModManager {

    private final FusionCore fusion;

    public ModManager(@NotNull final FusionCore fusion) {
        this.fusion = fusion;

        ModSupport.dependencies.forEach(dependency -> addMod(dependency, new Mod()));
    }

    public Map<Key, IMod> mods = new HashMap<>();

    public void addMod(@NotNull final Key key, @NotNull final Mod mod) {
        this.mods.putIfAbsent(key, mod.accept(consumer -> {
            consumer.setFusion(this.fusion);
            consumer.setKey(key);
        }));
    }

    public void removeMod(@NotNull final Key key) {
        if (!this.mods.containsKey(key) || ModSupport.dependencies.contains(key)) return;

        this.mods.remove(key);
    }

    public @NotNull final Map<Key, IMod> getMods() {
        return Collections.unmodifiableMap(this.mods);
    }

    public IMod getMod(@NotNull final Key key) {
        return this.mods.get(key);
    }
}