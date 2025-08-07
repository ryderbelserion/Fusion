package com.ryderbelserion.fusion.core.api.support;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.interfaces.mods.IMod;
import com.ryderbelserion.fusion.core.FusionKey;
import com.ryderbelserion.fusion.core.api.support.objects.Mod;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModManager {

    private final FusionCore fusion;

    public ModManager(@NotNull final FusionCore fusion) {
        this.fusion = fusion;
    }

    public Map<FusionKey, IMod> mods = new HashMap<>();

    public void addMod(@NotNull final FusionKey key, @NotNull final Mod mod) {
        this.mods.putIfAbsent(key, mod.accept(consumer -> {
            consumer.setFusion(this.fusion);
            consumer.setKey(key);
        }));
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