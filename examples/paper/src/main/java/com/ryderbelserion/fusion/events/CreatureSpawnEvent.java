package com.ryderbelserion.fusion.events;

import com.ryderbelserion.fusion.Fusion;
import com.ryderbelserion.fusion.kyori.utils.AdvUtils;
import com.ryderbelserion.fusion.paper.api.events.IPaperEvent;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CreatureSpawnEvent implements IPaperEvent {

    private final Fusion plugin = JavaPlugin.getPlugin(Fusion.class);

    private final ComponentLogger logger = this.plugin.getComponentLogger();

    @Override
    public void start() {
        this.logger.warn(AdvUtils.parse("<red>{} has been enabled!"), getName());
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(final org.bukkit.event.entity.CreatureSpawnEvent event) {
        if (!isEnabled()) return;

        this.logger.warn(AdvUtils.parse("<red>The entity {} has spawned!"), event.getEntity().getName());
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public @NotNull String getName() {
        return "Creature Spawn Event";
    }
}