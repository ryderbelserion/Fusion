package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.core.utils.FileUtils;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.plugin.java.JavaPlugin;
import java.nio.file.Path;
import java.util.List;

public class FusionPlugin extends JavaPlugin {

    public static FusionPlugin getPlugin() {
        return JavaPlugin.getPlugin(FusionPlugin.class);
    }

    private FusionPaper api = null;

    @Override
    public void onEnable() {
        final Path path = getDataPath();

        this.api = new FusionPaper(getComponentLogger(), path);
        this.api.enable(this);

        //FileUtils.extract("guis", path.resolve("examples"), true, false);
        //FileUtils.extract("logs", path.resolve("examples"), true, false);
        //FileUtils.extract("crates", path.resolve("examples"), true, false);
        //FileUtils.extract("schematics", path.resolve("examples"), true, false);

        FileUtils.extract("banners/icons", path, true, true);

        FileUtils.extract("mobs/icons", path, true, true);

        //List.of(
        //        "config.yml",
        //        "data.yml",
        //        "locations.yml",
        //        "messages.yml",
        //        "editor.yml"
        //).forEach(file -> FileUtils.extract(file, path.resolve("examples"), false, true));
    }

    public FusionPaper getApi() {
        return this.api;
    }
}