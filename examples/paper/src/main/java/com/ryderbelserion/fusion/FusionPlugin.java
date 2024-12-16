package com.ryderbelserion.fusion;

import com.ryderbelserion.FusionSettings;
import org.bukkit.plugin.java.JavaPlugin;

public class FusionPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        FusionSettings.get().register(this);
    }
}