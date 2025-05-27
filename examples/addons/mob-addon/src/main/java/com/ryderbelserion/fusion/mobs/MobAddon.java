package com.ryderbelserion.fusion.mobs;

import com.ryderbelserion.fusion.core.api.addons.interfaces.IAddon;
import com.ryderbelserion.fusion.core.api.interfaces.ILogger;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.FusionPlugin;
import java.nio.file.Path;

public class MobAddon extends IAddon {

    private final FusionPaper paper = (FusionPaper) this.fusion;

    private final ILogger logger = getLogger();

    @Override
    public void onEnable() {
        FusionPlugin.getPlugin().getComponentLogger().warn("This is a test!");

        this.logger.safe("<green>MobAddon is enabled");

        final Path path = this.getFolder();

        if (path.toFile().mkdirs()) {
            this.logger.safe("<green>Creating folder <yellow>{}", path.toAbsolutePath());
        }
    }

    @Override
    public void onDisable() {
        this.logger.safe("<green>MobAddon is disabled");
    }
}