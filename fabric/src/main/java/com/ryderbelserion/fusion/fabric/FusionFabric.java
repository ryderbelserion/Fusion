package com.ryderbelserion.fusion.fabric;

import com.ryderbelserion.fusion.core.api.FusionCore;
import com.ryderbelserion.fusion.fabric.api.commands.FabricCommandManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

public class FusionFabric extends FusionCore {

    private final FabricCommandManager manager;

    public FusionFabric(@NotNull final ComponentLogger logger, @NotNull final Path path) {
        super(logger, path);

        this.manager = new FabricCommandManager();
    }

    public final FabricCommandManager getManager() {
        return this.manager;
    }

    @Override
    public @NotNull final String parsePlaceholders(@NotNull final Audience audience, @NotNull final String message) {
        return "";
    }

    @Override
    public @NotNull final FabricCommandManager getCommandManager() {
        return this.manager;
    }
}