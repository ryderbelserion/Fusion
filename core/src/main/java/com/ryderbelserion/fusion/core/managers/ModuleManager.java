package com.ryderbelserion.fusion.core.managers;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.interfaces.IModule;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModuleManager {

    private final FusionCore api = FusionCore.Provider.get();

    private final List<IModule> modules = new ArrayList<>();

    public void load() {
        this.modules.forEach(module -> {
            if (module.isEnabled()) {
                this.api.registerEvent(module);

                module.enable();

                return;
            }

            module.disable();
        });
    }

    public void reload() {
        this.modules.forEach(module -> {
            if (module.isEnabled()) {
                module.reload();
            } else {
                module.disable();
            }
        });
    }

    public void unload(final boolean purge) {
        this.modules.forEach(module -> {
            if (module.isEnabled()) {
                module.disable();
            }
        });

        if (purge) {
            this.modules.clear();
        }
    }

    public void unload() {
        unload(false);
    }

    public void addModule(final IModule module) {
        if (hasModule(module)) return;

        this.modules.add(module);
    }

    public void removeModule(final IModule module) {
        if (!hasModule(module)) return;

        this.modules.remove(module);
    }

    public @NotNull final List<IModule> getModules() {
        return Collections.unmodifiableList(this.modules);
    }

    private boolean hasModule(@NotNull final IModule module) {
        final String name = module.getName();

        boolean hasModule = false;

        for (final IModule key : this.modules) {
            if (name.equals(key.getName())) {
                hasModule = true;
            }
        }

        return hasModule;
    }
}