package com.ryderbelserion;

import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.files.PaperFileManager;
import com.ryderbelserion.fusion.paper.files.types.PaperCustomFile;
import com.ryderbelserion.listeners.ItemListener;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.Optional;

public class CrazyCrates extends JavaPlugin {

    private FusionPaper fusion;

    @Override
    public void onEnable() {
        this.fusion = new FusionPaper(this);

        final PaperFileManager fileManager = this.fusion.getFileManager();

        //fileManager.addPaperFile(getDataPath().resolve("test.yml"), consumer -> consumer.addAction(FileAction.EXTRACT_FILE))
        //        .addFile(getDataPath().resolve("config.yml"),
        //        options -> options.indentationSize(2).charset(Charset.defaultCharset()),
        //        builder -> builder.configurationData(ConfigKeys.class));

        final Path crate = getDataPath().resolve("crates");

        fileManager.addPaperFolder(crate);

        final ComponentLogger logger = getComponentLogger();

        /*fileManager.getFiles().forEach(((path, file) -> {
            logger.warn("Path: {}", path);
        }));*/

        final Optional<PaperCustomFile> customFile = fileManager.getPaperFile(crate.resolve("AdvancedExample.yml"));

        if (customFile.isPresent()) {
            final PaperCustomFile paper = customFile.get();

            final YamlConfiguration configuration = paper.getConfiguration();

            configuration.set("Crate.CrateType", "Cosmic");

            logger.warn("Type: {}", configuration.get("Crate.CrateType", "CSGO"));

            fileManager.savePaperFile(paper);
        }

        final Server server = getServer();

        server.getPluginManager().registerEvents(new ItemListener(), this);
    }

    public @NotNull final FusionPaper getFusion() {
        return this.fusion;
    }
}