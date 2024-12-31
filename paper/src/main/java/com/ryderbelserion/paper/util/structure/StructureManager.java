package com.ryderbelserion.paper.util.structure;

import com.ryderbelserion.FusionApi;
import com.ryderbelserion.core.api.exception.FusionException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.plugin.Plugin;
import org.bukkit.structure.Structure;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class StructureManager {

    private final FusionApi fusionApi = FusionApi.get();

    private final Plugin plugin = this.fusionApi.getPlugin();
    private final Server server = this.plugin.getServer();
    private final org.bukkit.structure.StructureManager manager = this.server.getStructureManager();

    private final Set<BlockState> structureBlocks = new HashSet<>();
    private final Set<BlockState> initialBlocks = new HashSet<>();

    private final Structure structure;
    private final BlockVector vector;
    private final boolean isReady;
    private final NamespacedKey key;

    public StructureManager(@NotNull final NamespacedKey key) {
        this.key = key;

        this.structure = this.manager.loadStructure(this.key);

        this.isReady = this.structure != null;
        this.vector = this.structure != null ? this.structure.getSize() : null;
    }

    public StructureManager(@NotNull final String keyName) {
        this.structure = this.manager.createStructure();
        this.vector = null;

        this.key = new NamespacedKey(this.plugin, keyName);

        this.isReady = false;
    }

    public void saveStructure(@NotNull final File file, final Location uno, final Location dos, final boolean includeEntities, final boolean registerStructure) {
        if (uno == null || dos == null) {
            throw new FusionException("Cannot save structure as the file or one of the corners is null.");
        }

        this.structure.fill(uno, dos, includeEntities);

        try {
            this.manager.saveStructure(file, this.structure);
        } catch (IOException exception) {
            throw new FusionException("Failed to save structure.", exception);
        }

        if (registerStructure) {
            this.manager.registerStructure(this.key, this.structure);
        }
    }

    public void removeStructure(final boolean restoreInitialBlocks) { // remove structure
        if (!this.isReady) return;

        this.structureBlocks.forEach(state -> state.setType(Material.AIR));

        this.structureBlocks.clear();

        if (restoreInitialBlocks) {
            this.initialBlocks.forEach(state -> state.update(true));

            this.initialBlocks.clear();
        }
    }

    public void pasteStructure(@NotNull final Location location) {
        if (!this.isReady) return;

        try {
            populate(true, location);

            this.structure.place(location, false, StructureRotation.NONE, Mirror.NONE, 0, 1F, ThreadLocalRandom.current());

            populate(false, location);
        } catch (final Exception exception) {
            throw new FusionException("Failed to paste structure.", exception);
        }
    }

    public Set<BlockState> getInitialBlocks() {
        return this.initialBlocks;
    }

    public Set<BlockState> getStructureBlocks() {
        return this.structureBlocks;
    }

    public NamespacedKey getNamespacedKey() {
        return this.key;
    }

    public List<String> getBlacklist() {
        return new ArrayList<>();
    }

    public double getX() {
        return this.isReady ? this.vector.getX() : 0.0;
    }

    public double getY() {
        return this.isReady ? this.vector.getY() : 0.0;
    }

    public double getZ() {
        return this.isReady ? this.vector.getZ() : 0.0;
    }

    private void populate(final boolean isInitial, @NotNull final Location location) {
        for (int x = 0; x < getX(); x++) {
            for (int y = 0; y < getY(); y++) {
                for (int z = 0; z < getZ(); z++) {
                    final Block relativeLocation = location.getBlock().getRelative(x, y, z);

                    if (isInitial) {
                        this.initialBlocks.add(relativeLocation.getState(false));
                    } else {
                        this.structureBlocks.add(relativeLocation.getState(false));
                    }
                }
            }
        }
    }
}