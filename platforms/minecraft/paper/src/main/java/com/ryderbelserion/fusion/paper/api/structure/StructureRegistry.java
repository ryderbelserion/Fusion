package com.ryderbelserion.fusion.paper.api.structure;

import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.io.IOException;

public class StructureRegistry {

    private final StructureManager manager;
    private final Plugin plugin;

    public StructureRegistry(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.manager = this.plugin.getServer().getStructureManager();
    }

    public @Nullable Structure getStructure(@NotNull String key) {
        return this.manager.getStructure(new NamespacedKey(this.plugin, key));
    }

    public void registerStructure(@NotNull final File file, @NotNull final String name) {
        final StructureManager manager = this.manager;

        Structure structure;

        try {
            structure = manager.loadStructure(file);
        } catch (IOException exception) {
            throw new FusionException("Failed to load structure.", exception);
        }

        final NamespacedKey key = new NamespacedKey(this.plugin, name);

        manager.registerStructure(key, structure);
    }

    public void unregisterStructure(@NotNull final String name) {
        if (getStructure(name) == null) return;

        final StructureManager manager = this.manager;

        manager.unregisterStructure(new NamespacedKey(this.plugin, name));
    }
}