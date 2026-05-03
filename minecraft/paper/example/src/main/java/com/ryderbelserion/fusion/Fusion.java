package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.commands.BaseCommand;
import com.ryderbelserion.fusion.api.PaperCommandManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;

public class Fusion extends JavaPlugin implements Listener {

    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        this.commandManager = new PaperCommandManager(this);

        List.of(
                new BaseCommand()
        ).forEach(command -> this.commandManager.parse(command));
    }
}