package com.ryderbelserion;

import com.ryderbelserion.commands.brigadier.BaseCommand;
import com.ryderbelserion.fusion.core.api.enums.FileAction;
import com.ryderbelserion.fusion.core.api.enums.FileType;
import com.ryderbelserion.fusion.core.api.utils.FileUtils;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.files.FileManager;
import com.ryderbelserion.fusion.paper.files.types.PaperCustomFile;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrazyCrates extends JavaPlugin {

    private FusionPaper fusion;

    @Override
    public void onEnable() {
        final Path dataPath = getDataPath();

        this.fusion = new FusionPaper(getComponentLogger(), dataPath);
        this.fusion.enable(this);

        final FileManager fileManager = this.fusion.getFileManager();

        fileManager.addFolder(dataPath.resolve("crates"), FileType.PAPER, new ArrayList<>(), null);

        final ComponentLogger logger = getComponentLogger();

        fileManager.getCustomFiles().forEach((path, custom) -> {
            logger.warn("File Name: {}, Pretty Name: {}, Path: {}", custom.getFileName(), custom.getPrettyName(), custom.getPath());

            final PaperCustomFile file = (PaperCustomFile) custom;

            final YamlConfiguration configuration = file.getConfiguration();

            final String type = configuration.getString("Crate.CrateType", "CSGO");

            logger.warn("Crate Type: {}", type);

            configuration.set("Crate.CrateType", UUID.randomUUID().toString());

            file.save();

            logger.warn("Crate Type: {}", configuration.getString("Crate.CrateType", "CSGO"));
        });

        final List<FileAction> actions = new ArrayList<>();

        actions.add(FileAction.DELETE_FILE);
        actions.add(FileAction.EXTRACT_FOLDER);

        FileUtils.extract("vouchers", dataPath.resolve("examples"), actions);
        FileUtils.extract("codes", dataPath.resolve("examples"), actions);

        actions.remove(FileAction.EXTRACT_FOLDER);

        List.of(
                "codes.yml",
                "vouchers.yml"
        ).forEach(file -> FileUtils.extract(file, dataPath.resolve("examples"), actions));

        this.fusion.getCommandManager().enable(new BaseCommand(), "A crazy plugin!", List.of("cc"));
    }

    public @NotNull final FusionPaper getFusion() {
        return this.fusion;
    }

    public @NotNull final FileManager getFileManager() {
        return this.fusion.getFileManager();
    }
}