package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Fusion extends JavaPlugin implements Listener {

    private FusionPaper fusion;

    @Override
    public void onEnable() {
        this.fusion = new FusionPaper(this);
        this.fusion.init();
    }

    public @NotNull final FusionPaper getFusion() {
        return this.fusion;
    }
}