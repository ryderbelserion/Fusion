package com.ryderbelserion.paper.util.structure;

import com.ryderbelserion.FusionApi;
import com.ryderbelserion.api.exception.FusionException;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.io.IOException;

public class StructureMethods {

    private static final FusionApi fusionApi = FusionApi.get();

    private static final Plugin plugin = fusionApi.getPlugin();
    private static final Server server = plugin.getServer();

    public static @Nullable Structure getStructure(@NotNull final String key) {
        return server.getStructureManager().getStructure(new NamespacedKey(plugin, key));
    }

    public static void registerStructure(@NotNull final File file, @NotNull final String name) {
        final StructureManager manager = server.getStructureManager();

        Structure structure;

        try {
            structure = manager.loadStructure(file);
        } catch (IOException exception) {
            throw new FusionException("Failed to load structure.", exception);
        }

        final NamespacedKey key = new NamespacedKey(plugin, name);

        manager.registerStructure(key, structure);
    }

    public static void unregisterStructure(@NotNull final String name) {
        if (getStructure(name) == null) return;

        final StructureManager manager = server.getStructureManager();

        manager.unregisterStructure(new NamespacedKey(plugin, name));
    }
}