package com.ryderbelserion;

import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;

public class CrazyCrates extends JavaPlugin {

    private FusionPaper fusion;

    @Override
    public void onEnable() {
        this.fusion = new FusionPaper(getComponentLogger(), getDataPath());
        this.fusion.enable(this);

        this.fusion.getFileManager().addFile(getDataPath().resolve("config.yml"), new ArrayList<>(), null);
    }
}