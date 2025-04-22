package com.ryderbelserion.fusion.modules;

import com.ryderbelserion.fusion.FusionPlugin;
import com.ryderbelserion.fusion.core.api.interfaces.IModule;
import com.ryderbelserion.fusion.core.utils.AdvUtils;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class TestModule implements IModule, Listener {

    private final FusionPlugin plugin = JavaPlugin.getPlugin(FusionPlugin.class);

    private final ComponentLogger logger = this.plugin.getComponentLogger();

    public void enable() {
        this.logger.warn(AdvUtils.parse("<red>The {} has enabled!"), getName());
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawnEvent(final CreatureSpawnEvent event) {
        if (!isEnabled()) return; // if false, return!

        this.logger.warn(AdvUtils.parse("<red>The entity {} has loaded!"), event.getEntity().getName());
    }

    @Override
    public @NotNull final String getName() {
        return "test_module";
    }
}