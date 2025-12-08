package com.ryderbelserion.fusion.addons.api.interfaces;

import com.ryderbelserion.fusion.addons.api.ExtensionMeta;

public abstract class IExtension extends ExtensionMeta {

    public IExtension() {}

    public void onEnable() {}

    public void onDisable() {}

    public void onReload() {}

    public abstract void setEnabled(final boolean isEnabled);

    public abstract boolean isEnabled();

}