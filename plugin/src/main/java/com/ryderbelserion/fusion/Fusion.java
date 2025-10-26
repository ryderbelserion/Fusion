package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.plugin.java.JavaPlugin;

public class Fusion extends JavaPlugin {

    @Override
    public void onEnable() {
        final FusionPaper fusion = new FusionPaper(getDataPath());
    }
}