package me.corecraft.currency;

import com.ryderbelserion.fusion.addons.v2.api.Extension;

public class Currency extends Extension {

    @Override
    public void onEnable() {
        super.onEnable();

        getLogger().warn("The extension is enabled!");
    }

    @Override
    public void onDisable() {
        super.onDisable();

        getLogger().warn("The extension is disabled!");
    }
}