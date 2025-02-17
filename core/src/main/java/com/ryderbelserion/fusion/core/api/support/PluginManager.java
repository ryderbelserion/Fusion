package com.ryderbelserion.fusion.core.api.support;

import com.ryderbelserion.fusion.core.FusionLayout;
import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.api.support.interfaces.IPlugin;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PluginManager {

    private static final FusionLayout api = FusionProvider.get();
    private static final ComponentLogger logger = api.getLogger();
    private static final boolean isVerbose = api.isVerbose();

    private static final Map<String, IPlugin> plugins = new HashMap<>();

    public PluginManager() {}

    public static void registerPlugin(@NotNull final IPlugin plugin) {
        plugins.put(plugin.getName(), plugin.init());
    }

    public static @Nullable IPlugin getPlugin(@NotNull final String name) {
        return plugins.get(name);
    }

    public static boolean isEnabled(@NotNull final String name) {
        final IPlugin plugin = getPlugin(name);

        return plugin != null && plugin.isEnabled();
    }

    public static void unregisterPlugin(@NotNull final IPlugin plugin) {
        plugins.remove(plugin.getName());

        plugin.stop();
    }

    public static void printPlugins() {
        if (isVerbose) {
            getPlugins().forEach((name, plugin) -> {
                if (plugin.isEnabled() && !name.isEmpty()) {
                    logger.info("{}: FOUND", name);
                } else {
                    logger.info("{}: NOT FOUND", name);
                }
            });
        }
    }

    public static @NotNull Map<String, IPlugin> getPlugins() {
        return Collections.unmodifiableMap(plugins);
    }
}