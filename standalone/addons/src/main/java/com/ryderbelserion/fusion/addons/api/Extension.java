package com.ryderbelserion.fusion.addons.api;

import com.ryderbelserion.fusion.addons.api.interfaces.IExtension;

public class Extension extends IExtension {

    public Extension() {}

    @Override
    public void post() {
        super.post();

        setEnabled(true);
    }

    private boolean isEnabled = false;

    @Override
    public final void setEnabled(final boolean isEnabled) {
        if (this.isEnabled != isEnabled) {
            this.isEnabled = isEnabled;

            if (this.isEnabled) {
                onEnable();
            } else {
                onDisable();
            }
        }
    }

    @Override
    public final boolean isEnabled() {
        return this.isEnabled;
    }
}