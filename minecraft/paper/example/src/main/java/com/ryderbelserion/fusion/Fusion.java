package com.ryderbelserion.fusion;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.commands.SimpleCommand;
import com.ryderbelserion.fusion.commands.types.ItemCommand;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.paper.FusionPaper;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.nio.file.Path;
import java.util.List;

public class Fusion extends JavaPlugin implements Listener {

    private FusionPaper fusion;

    @Override
    public void onEnable() {
        this.fusion = new FusionPaper(this);
        this.fusion.init();

        final FileManager fileManager = this.fusion.getFileManager();

        final Path path = getDataPath();

        fileManager.extractFolder("crates", path);

        //fileManager.extractFile("velocity/config.yml", path.resolve("configx2.yml"));

        fileManager.addFile(path.resolve("config.yml"), "velocity", FileType.YAML);

        fileManager.addFolder(path.resolve("locale"), "velocity", FileType.YAML);

        fileManager.getYamlFile(path.resolve("config.yml")).ifPresent(customFile -> {
            final CommentedConfigurationNode node = customFile.getConfiguration();

            this.fusion.log(Level.WARNING, "Node: %s, %s", node.node("test").getBoolean(false), node.node("beans").getBoolean(true));
        });

        fileManager.getYamlFile(path.resolve("locale").resolve("de-DE.yml")).ifPresent(customFile -> {
            final CommentedConfigurationNode node = customFile.getConfiguration();

            this.fusion.log(Level.WARNING, "Node: %s", node.node("messages", "reload-plugin").getString("{prefix}<yellow>Das Plugin wurde neu geladen."));
        });

        final LifecycleEventManager<Plugin> eventManager = getLifecycleManager();

        // Register commands.
        eventManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            LiteralArgumentBuilder<CommandSourceStack> root = new SimpleCommand(this).registerPermissions().literal().createBuilder();

            List.of(
                    new ItemCommand(this)
            ).forEach(type -> root.then(type.literal()));

            event.registrar().register(root.build(), "The base command for Fusion!");
        });
    }

    public @NonNull final FusionPaper getFusion() {
        return this.fusion;
    }
}