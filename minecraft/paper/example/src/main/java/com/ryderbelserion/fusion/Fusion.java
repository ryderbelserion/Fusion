package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.commands.BaseCommand;
import com.ryderbelserion.fusion.paper.commands.PaperCommandManager;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;

public class Fusion extends JavaPlugin implements Listener {

    private PaperCommandManager commandManager;
    private FusionPaper fusion;

    @Override
    public void onEnable() {
        this.fusion = new FusionPaper(this);
        this.fusion.init();

        this.commandManager = new PaperCommandManager(this);
        this.commandManager.init();

        List.of(
                new BaseCommand()
        ).forEach(command -> this.commandManager.parse(command));
    }
}