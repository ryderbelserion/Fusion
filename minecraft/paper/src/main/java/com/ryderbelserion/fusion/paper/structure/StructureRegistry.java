package com.ryderbelserion.fusion.paper.structure;

import com.ryderbelserion.fusion.core.exceptions.FusionException;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class StructureRegistry {

    private final StructureManager manager;
    private final JavaPlugin plugin;

    public StructureRegistry(@NotNull final JavaPlugin plugin, @NotNull final StructureManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    public @NotNull Optional<@Nullable Structure> getStructure(@NotNull final String key) {
        return Optional.ofNullable(this.manager.getStructure(new NamespacedKey(this.plugin, key)));
    }

    public void registerStructure(@NotNull final Path path, @NotNull final String name) {
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
        getStructure(name).ifPresent(structure -> {
            final StructureManager manager = this.manager;

            manager.unregisterStructure(new NamespacedKey(this.plugin, name));
        });
    }
}