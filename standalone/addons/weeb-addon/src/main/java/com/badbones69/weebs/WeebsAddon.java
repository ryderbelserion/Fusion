package com.badbones69.weebs;

import com.ryderbelserion.fusion.addons.api.Extension;

public class WeebsAddon extends Extension {

    @Override
    public void onEnable() {
        getLogger().info("Guten Tag!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Guten Nacht!");
    }
}