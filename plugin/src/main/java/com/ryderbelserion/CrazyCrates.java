package com.ryderbelserion;

import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.types.JaluCustomFile;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.keys.ConfigKeys;
import com.ryderbelserion.listeners.ItemListener;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.nio.charset.Charset;
import java.util.Optional;

public class CrazyCrates extends JavaPlugin {

    private FusionPaper fusion;

    @Override
    public void onEnable() {
        this.fusion = new FusionPaper(this, "crazycrates");

        final FileManager fileManager = this.fusion.getFileManager();

        fileManager.addFile(getDataPath().resolve("config.yml"),
                options -> options.indentationSize(2).charset(Charset.defaultCharset()),
                builder -> builder.configurationData(ConfigKeys.class));

        final ComponentLogger logger = getComponentLogger();

        final Optional<JaluCustomFile> customFile = fileManager.getJaluFile(getDataPath().resolve("config.yml"));

        if (customFile.isPresent()) {
            final SettingsManager config = customFile.get().getConfiguration();

            logger.warn("Test: {}", config.getProperty(ConfigKeys.test));
        }

        getServer().getPluginManager().registerEvents(new ItemListener(), this);
    }

    public @NotNull final FusionPaper getFusion() {
        return this.fusion;
    }
}