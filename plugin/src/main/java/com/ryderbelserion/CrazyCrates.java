package com.ryderbelserion;

import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;

public class CrazyCrates extends JavaPlugin {

    @Override
    public void onEnable() {
        FusionPaper fusion = new FusionPaper(getComponentLogger(), getDataPath());

        fusion.enable(this);

        FileManager fileManager = fusion.getFileManager();

        fileManager.addFile(getDataPath().resolve("config.yml"), new ArrayList<>(), options -> options.shouldCopyDefaults(true));
    }
}