package com.ryderbelserion;

import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CrazyCrates extends JavaPlugin {

    private FusionPaper fusion;

    @Override
    public void onEnable() {
        this.fusion = new FusionPaper(this);
    }

    public @NotNull final FusionPaper getFusion() {
        return this.fusion;
    }
}