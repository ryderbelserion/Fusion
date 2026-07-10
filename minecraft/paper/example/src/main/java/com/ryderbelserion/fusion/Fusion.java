package com.ryderbelserion.fusion;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.fusion.commands.SimpleCommand;
import com.ryderbelserion.fusion.commands.types.items.ItemCommand;
import com.ryderbelserion.fusion.commands.types.ReloadCommand;
import com.ryderbelserion.fusion.commands.types.items.SkullCommand;
import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.enums.Level;
import com.ryderbelserion.fusion.core.api.registry.message.MessageRegistry;
import com.ryderbelserion.fusion.core.api.registry.message.adapter.YamlMessageAdapter;
import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.files.PaperFileManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.configuration.file.YamlConfiguration;
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

        final PaperFileManager fileManager = this.fusion.getFileManager();

        final Path path = getDataPath();

        fileManager.addPaperFolder(path.resolve("crates"))
                .addPaperFile(path.resolve("guis").resolve("test.yml"), consumer -> consumer.addAction(FileAction.EXTRACT_FROM_FOLDER))
                .addFolder(path.resolve("locale"), "velocity", FileType.YAML)
                .addFolder(path.resolve("discord"), FileType.YAML)
                .extractFolder("icons", "velocity", FileType.PNG, path)
                .extractFile("velocity/config.yml", path.resolve("config.yml"))
                .addFile(path.resolve("config.yml"), "velocity", FileType.YAML)
                .addFile(path.resolve("test.yml"), FileType.YAML)
                .getFilesByPath(path.resolve("crates"), ".yml").forEach(target ->
                        fileManager.getPaperFile(target).ifPresent(customFile -> this.fusion.log(Level.WARNING, "<red>Custom File: %s", customFile.getPrettyName())));

        fileManager.getYamlFile(path.resolve("config.yml")).ifPresent(customFile -> {
            final CommentedConfigurationNode node = customFile.getConfiguration();

            this.fusion.log(Level.WARNING, "Node: %s, %s", node.node("test").getBoolean(false), node.node("beans").getBoolean(true));
        });

        fileManager.getFilesByPath(path.resolve("crates"), ".yml").forEach(parent -> {
            fileManager.getPaperFile(parent).ifPresent(customFile -> {
                final YamlConfiguration configuration = customFile.getConfiguration();

                this.fusion.log(Level.WARNING, "Crate Type: %s", configuration.getString("Crate.CrateType", "CSGO"));
            });
        });

        fileManager.getYamlFile(path.resolve("locale").resolve("de-DE.yml")).ifPresent(customFile -> {
            final CommentedConfigurationNode node = customFile.getConfiguration();

            this.fusion.log(Level.WARNING, "Node: %s", node.node("messages", "reload-plugin").getString("{prefix}<yellow>Das Plugin wurde neu geladen."));

            final MessageRegistry registry = this.fusion.getMessageRegistry();

            registry.addKey(FusionKey.key(this.fusion.getNamespace(), "reload_plugin"), new YamlMessageAdapter(node, "{prefix}<yellow>Das Plugin wurde neu geladen. %player_ping%", "messages", "reload-plugin"));
        });

        List.of(
                "test.yml"
        ).forEach(input -> fileManager.extractFile(input, path.resolve("examples").resolve("test.yml")));

        fileManager.saveFile(path.resolve("test.yml"));

        final LifecycleEventManager<Plugin> eventManager = getLifecycleManager();

        // Register commands.
        eventManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            LiteralArgumentBuilder<CommandSourceStack> root = new SimpleCommand(this).registerPermissions().literal().createBuilder();

            List.of(
                    new ItemCommand(this),
                    new ReloadCommand(this),
                    new SkullCommand(this)
            ).forEach(type -> root.then(type.literal()));

            event.registrar().register(root.build(), "The base command for Fusion!");
        });
    }

    public @NonNull final FusionPaper getFusion() {
        return this.fusion;
    }
}