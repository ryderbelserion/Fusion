package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.chat.ChatListener;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Fusion extends JavaPlugin implements Listener {

    private final FusionPaper fusion;

    public Fusion(@NotNull final FusionPaper fusion) {
        this.fusion = fusion;
    }

    @Override
    public void onEnable() {
        this.fusion.setPlugin(this).init();

        getServer().getPluginManager().registerEvents(new ChatListener(this.fusion), this);
    }
}