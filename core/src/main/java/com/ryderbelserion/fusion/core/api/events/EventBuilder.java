package com.ryderbelserion.fusion.core.api.events;

import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.events.interfaces.IEvent;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventBuilder {

    private final FusionCore api = FusionCore.Provider.get();

    private final List<IEvent> modules = new ArrayList<>();

    public void load() {
        this.modules.forEach(module -> {
            if (module.isEnabled()) {
                this.api.registerEvent(module);

                module.start();

                return;
            }

            module.stop();
        });
    }

    public void reload() {
        this.modules.forEach(module -> {
            if (module.isEnabled()) {
                module.restart();
            } else {
                module.stop();
            }
        });
    }

    public void unload(final boolean purge) {
        this.modules.forEach(module -> {
            if (module.isEnabled()) {
                module.stop();
            }
        });

        if (purge) {
            this.modules.clear();
        }
    }

    public void unload() {
        unload(false);
    }

    public void addModule(final IEvent module) {
        if (hasModule(module)) return;

        this.modules.add(module);
    }

    public void removeModule(final IEvent module) {
        if (!hasModule(module)) return;

        this.modules.remove(module);
    }

    public @NotNull final List<IEvent> getModules() {
        return Collections.unmodifiableList(this.modules);
    }

    private boolean hasModule(@NotNull final IEvent module) {
        final String name = module.getName();

        boolean hasModule = false;

        for (final IEvent key : this.modules) {
            if (name.equals(key.getName())) {
                hasModule = true;
            }
        }

        return hasModule;
    }
}