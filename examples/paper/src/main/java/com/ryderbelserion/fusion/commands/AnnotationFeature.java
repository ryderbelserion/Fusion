package com.ryderbelserion.fusion.commands;

import com.ryderbelserion.fusion.Fusion;
import com.ryderbelserion.fusion.core.files.FileManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.annotations.AnnotationParser;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

public abstract class AnnotationFeature {

    protected final Fusion plugin = JavaPlugin.getPlugin(Fusion.class);

    protected final FileManager fileManager = this.plugin.getFileManager();

    protected final ComponentLogger logger = this.plugin.getComponentLogger();

    protected final Path path = this.plugin.getDataPath();

    public abstract void registerFeature(@NotNull final AnnotationParser<CommandSourceStack> parser);

}