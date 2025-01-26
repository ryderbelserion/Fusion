package com.ryderbelserion.paper.modules;

import com.ryderbelserion.paper.modules.interfaces.IPaperModule;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class handling the loading of modules.
 *
 * @author ryderbelserion
 * @version 0.9.0
 * @since 0.9.0
 */
public class ModuleLoader {

    private final List<IPaperModule> modules = new ArrayList<>();

    private final EventRegistry registry;

    /**
     * Creates the module loader
     *
     * @param registry {@link EventRegistry}
     * @since 0.9.0
     */
    public ModuleLoader(@NotNull final EventRegistry registry) {
        this.registry = registry;
    }

    /**
     * Loads modules onto the server.
     * @since 0.9.0
     */
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

    /**
     * Reload modules if enabled.
     * @since 0.9.0
     */
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

    /**
     * Unregister modules if enabled.
     *
     * @param purge clears all modules
     * @since 0.9.0
     */
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

    /**
     * Unregister modules if enabled.
     * @since 0.9.0
     */
    public void unload() {
        unload(false);
    }

    /**
     * Add a module to the map.
     *
     * @param module the {@link com.ryderbelserion.core.api.interfaces.IModule}
     * @since 0.9.0
     */
    public void addModule(@NotNull final IPaperModule module) {
        if (hasModule(module)) return;

        this.modules.add(module);
    }

    /**
     * Remove a module from the map.
     *
     * @param module {@link com.ryderbelserion.core.api.interfaces.IModule}
     * @since 0.9.0
     */
    public void removeModule(@NotNull final IPaperModule module) {
        if (!hasModule(module)) return;

        this.modules.remove(module);
    }

    /**
     * Grab an unmodifiable list of active modules.
     *
     * @return the list of active modules.
     * @since 0.9.0
     */
    public @NotNull final List<IPaperModule> getModules() {
        return Collections.unmodifiableList(this.modules);
    }

    /**
     * Get the event registry instance.
     *
     * @return {@link EventRegistry}
     * @since 0.9.0
     */
    public @NotNull final EventRegistry getRegistry() {
        return this.registry;
    }

    /**
     * Check if the module is in the modules map.
     *
     * @param module {@link com.ryderbelserion.core.api.interfaces.IModule}
     * @return true or false
     * @since 0.9.0
     */
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