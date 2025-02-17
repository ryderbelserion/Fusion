package com.ryderbelserion.paper.fusion.modules;

import com.ryderbelserion.paper.fusion.modules.interfaces.IPaperModule;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModuleLoader {

    private final List<IPaperModule> modules = new ArrayList<>();

    private final EventRegistry registry;

    public ModuleLoader(@NotNull final EventRegistry registry) {
        this.registry = registry;
    }

    public void load() {
        this.modules.forEach(module -> {
            if (module.isEnabled()) {
                this.registry.addEvent(module);
                
                module.enable();

                return;
            }

            module.disable();
        });
    }

    public void reload() {
        this.modules.forEach(module -> {
            if (module.isEnabled()) {
                this.registry.addEvent(module);

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

    public void addModule(@NotNull final IPaperModule module) {
        if (hasModule(module)) return;

        this.modules.add(module);
    }

    public void removeModule(@NotNull final IPaperModule module) {
        if (!hasModule(module)) return;

        this.modules.remove(module);
    }

    public @NotNull final List<IPaperModule> getModules() {
        return Collections.unmodifiableList(this.modules);
    }

    public @NotNull final EventRegistry getRegistry() {
        return this.registry;
    }

    private boolean hasModule(@NotNull final IPaperModule module) {
        final String name = module.getName();

        boolean hasModule = false;

        for (final IPaperModule key : this.modules) {
            if (name.equals(key.getName())) {
                hasModule = true;
            }
        }

        return hasModule;
    }
}