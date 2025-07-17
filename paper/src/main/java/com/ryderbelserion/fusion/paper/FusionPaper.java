package com.ryderbelserion.fusion.paper;

import com.ryderbelserion.fusion.core.FusionCore;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class FusionPaper extends FusionCore {

    public FusionPaper(@NotNull final JavaPlugin plugin) {
        super(consumer -> {
            consumer.setDataPath(plugin.getDataPath());
        });
    }
}