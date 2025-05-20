package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.core.api.events.EventBuilder;
import com.ryderbelserion.fusion.events.CreatureSpawnEvent;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.plugin.java.JavaPlugin;

public class Fusion extends JavaPlugin {

    private FusionPaper paper;

    @Override
    public void onEnable() {
        this.paper = new FusionPaper(this);

        final EventBuilder builder = this.paper.getEventBuilder();

        builder.addModule(new CreatureSpawnEvent()).load();
    }

    public final FusionPaper getPaper() {
        return this.paper;
    }
}