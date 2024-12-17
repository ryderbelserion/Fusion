package com.ryderbelserion.paper.util.structure;

import com.ryderbelserion.FusionApi;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.structure.Structure;
import org.jetbrains.annotations.Nullable;

public class StructureMethods {

    private static final FusionApi fusionApi = FusionApi.get();

    private static final Plugin plugin = fusionApi.getPlugin();
    private static final Server server = plugin.getServer();

    public static @Nullable Structure getStructure(final String key) {
        return server.getStructureManager().getStructure(new NamespacedKey(plugin, key));
    }
}