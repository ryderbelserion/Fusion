package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.plugin.java.JavaPlugin;

public class Fusion extends JavaPlugin {

    private FusionPaper paper;

    @Override
    public void onEnable() {
        this.paper = new FusionPaper(this);
    }

    public final FusionPaper getPaper() {
        return this.paper;
    }
}