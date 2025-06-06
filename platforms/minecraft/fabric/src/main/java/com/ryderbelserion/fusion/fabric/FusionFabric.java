package com.ryderbelserion.fusion.fabric;

import ch.jalu.configme.SettingsManagerBuilder;
import com.ryderbelserion.fusion.fabric.api.commands.FabricCommandManager;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import java.net.URL;
import java.nio.file.Path;
import java.util.function.Consumer;

public class FusionFabric extends FusionKyori {

    private final FabricCommandManager manager;

    public FusionFabric(@NotNull final ComponentLogger logger, @NotNull final Path path, @NotNull final Consumer<SettingsManagerBuilder> consumer) {
        super(logger, path, consumer);

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
    public @NotNull final String chomp(@NotNull final String message) {
        return StringUtils.chomp(message);
    }

    @Override
    public @NotNull final FabricCommandManager getCommandManager() {
        return this.manager;
    }

    @Override
    public @NotNull final URL getClassPath() {
        return getClass().getProtectionDomain().getCodeSource().getLocation();
    }
}