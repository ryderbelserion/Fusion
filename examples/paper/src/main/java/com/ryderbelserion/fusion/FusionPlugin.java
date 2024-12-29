package com.ryderbelserion.fusion;

import com.ryderbelserion.FusionApi;
import com.ryderbelserion.api.enums.FileType;
import com.ryderbelserion.files.CustomFile;
import com.ryderbelserion.files.FileManager;
import com.ryderbelserion.files.types.JsonCustomFile;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.BasicConfigurationNode;

public class FusionPlugin extends JavaPlugin {

    private final FusionApi fusionApi = FusionApi.get();

    @Override
    public void onEnable() {
        this.fusionApi.enable(this);

        final FileManager fileManager = new FileManager();

        fileManager.addFile("config.json", FileType.JSON);

        final JsonCustomFile node = (JsonCustomFile) fileManager.getFile("config.json", FileType.JSON);

        if (node == null) {
            getComponentLogger().warn("Could not load config.json");

            return;
        }

        final BasicConfigurationNode configuration = node.getBasicConfigurationNode();

        if (configuration == null) {
            getComponentLogger().warn("Configuration node is null!");

            return;
        }

        final ComponentLogger logger = getComponentLogger();

        logger.warn("Configuration node has been loaded!");

        logger.warn("Beans: " + configuration.node("beans").getBoolean());
        logger.warn("other-option: " + configuration.node("other-option").getString());
    }

    @Override
    public void onDisable() {
        this.fusionApi.disable();
    }

    public @NotNull FusionApi getFusionSettings() {
        return this.fusionApi;
    }
}