package com.ryderbelserion.fusion.paper.api.structure;

import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.io.IOException;

public class StructureRegistry {

    private final StructureManager manager;
    private final JavaPlugin plugin;

    public StructureRegistry(@NotNull final JavaPlugin plugin) {
        this.plugin = plugin;
        this.manager = this.plugin.getServer().getStructureManager();
    }

    public @Nullable Structure getStructure(@NotNull final String key) {
        return this.manager.getStructure(new NamespacedKey(this.plugin, key));
    }

    public void registerStructure(@NotNull final File file, @NotNull final String name) {
        final StructureManager manager = this.manager;

        CompletableFuture.supplyAsync(() -> {
            try {
                return manager.loadStructure(path.toFile());
            } catch (final IOException exception) {
                throw new FusionException("Failed to load structure!", exception);
            }
        }).thenAccept(structure -> {
            final NamespacedKey key = new NamespacedKey(this.plugin, name);

            manager.registerStructure(key, structure);
        });
    }

    public void unregisterStructure(@NotNull final String name) {
        if (getStructure(name) == null) return;

        final StructureManager manager = this.manager;

        manager.unregisterStructure(new NamespacedKey(this.plugin, name));
    }
}