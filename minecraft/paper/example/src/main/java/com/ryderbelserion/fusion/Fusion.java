package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.commands.BaseCommand;
import com.ryderbelserion.fusion.paper.commands.PaperCommandManager;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;

public class Fusion extends JavaPlugin implements Listener {

    private FusionPaper fusion;

    @Override
    public void onEnable() {
        this.fusion = new FusionPaper(this);
        this.fusion.init();

        final PaperCommandManager manager = this.fusion.getCommandManager();

        List.of(
                new BaseCommand()
        ).forEach(manager::parse);
    }
}