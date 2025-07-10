package com.ryderbelserion;

import com.ryderbelserion.fusion.core.api.enums.FileType;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.files.FileManager;
import com.ryderbelserion.fusion.paper.files.types.PaperCustomFile;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;

public class CrazyCrates extends JavaPlugin {

    private FusionPaper fusion;

    @Override
    public void onEnable() {
        this.fusion = new FusionPaper(getComponentLogger(), getDataPath());
        this.fusion.enable(this);

        final FileManager fileManager = this.fusion.getFileManager();

        fileManager.addFolder(getDataPath().resolve("crates"), FileType.PAPER, new ArrayList<>(), null);

        final ComponentLogger logger = getComponentLogger();

        fileManager.getCustomFiles().forEach((path, custom) -> {
            logger.warn("File Name: {}, Pretty Name: {}, Path: {}", custom.getFileName(), custom.getPrettyName(), custom.getPath());

            final PaperCustomFile file = (PaperCustomFile) custom;

            final YamlConfiguration configuration = file.getConfiguration();

            final String type = configuration.getString("Crate.CrateType", "CSGO");

            logger.warn("Crate Type: {}", type);
        });
    }
}