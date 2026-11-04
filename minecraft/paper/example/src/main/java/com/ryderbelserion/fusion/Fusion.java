package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.api.enums.Level;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.objects.FileData;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;
import java.nio.file.Path;

public class Fusion extends JavaPlugin implements Listener {

    private FusionPaper fusion;

    @Override
    public void onEnable() {
        this.fusion = new FusionPaper(this);
        this.fusion.init();

        final Path path = getDataPath();

        final FileManager fileManager = this.fusion.getFileManager();

        fileManager.addFolder(path.resolve("crates"), FileType.YAML)
                .addFile(path.resolve("guis").resolve("test.yml"), FileType.YAML, consumer -> consumer.addAction(FileAction.EXTRACT_FROM_FOLDER))
                .addFolder(path.resolve("locale"), "velocity", FileType.YAML)
                .addFolder(path.resolve("discord"), FileType.YAML)
                .extractFolder("icons", "velocity", FileType.PNG, path)
                .extractFile("velocity/config.yml", path.resolve("config.yml"))
                .addFile(path.resolve("config.yml"), "velocity", FileType.YAML)
                .addFile(path.resolve("test.yml"), FileType.YAML)
                .getFilesByPath(path.resolve("crates"), ".yml", 1).forEach(target ->
                        fileManager.getFile(target).ifPresent(customFile -> this.fusion.log(Level.warn, "<red>Custom File: %s", customFile.getPrettyName())));

        //final Path path = getDataPath();

        //new ConfigManager(this.fusion, path).init();

        /*
        fileManager.getYamlFile(path.resolve("config.yml")).ifPresent(customFile -> {
            final CommentedConfigurationNode node = customFile.getConfiguration();

            this.fusion.log(Level.warn, "Node: %s, %s",
                    node.node("test").getBoolean(false), node.node("beans").getBoolean(true));
        });

        fileManager.getFilesByPath(path.resolve("crates"), ".yml").forEach(parent -> {
            fileManager.getYamlFile(parent).ifPresent(customFile -> {
                final CommentedConfigurationNode configuration = customFile.getConfiguration();

                this.fusion.log(Level.warn, "Crate Type: %s",
                        configuration.node("Crate", "CrateType").getString("CSGO"));
            });
        });

        fileManager.getYamlFile(path.resolve("locale").resolve("de-DE.yml")).ifPresent(customFile -> {
            final CommentedConfigurationNode node = customFile.getConfiguration();

            this.fusion.log(Level.warn, "Node: %s",
                    node.node("messages", "reload-plugin").getString("{prefix}<yellow>Das Plugin wurde neu geladen."));

            final MessageRegistry registry = this.fusion.getMessageRegistry();

            registry.addKey(FusionKey.key(this.fusion.getNamespace(), "reload_plugin"),
                    new YamlMessageAdapter(node,
                            "{prefix}<yellow>Das Plugin wurde neu geladen. %player_ping%",
                            "messages", "reload-plugin"));
        });

        List.of(
                "test.yml"
        ).forEach(input -> fileManager.extractFile(input, path.resolve("examples").resolve("test.yml")));

        fileManager.getYamlFile(path.resolve("test.yml")).ifPresent(customFile -> {
            final CommentedConfigurationNode configuration = customFile.getConfiguration().node("Locations");

            try {
                configuration.node("1", "World").set(String.class, "world");
                configuration.node("1", "X").set(int.class, 1);
                configuration.node("1", "Y").set(int.class, 2);
                configuration.node("1", "Z").set(int.class, 3);

                customFile.save();
            } catch (SerializationException e) {
                throw new RuntimeException(e);
            }
        });

        fileManager.saveFile(path.resolve("test.yml"));*/

        /*final LifecycleEventManager<Plugin> eventManager = getLifecycleManager();

        // Register commands.
        eventManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            LiteralArgumentBuilder<CommandSourceStack> root = new SimpleCommand(this).registerPermissions().literal().createBuilder();

            List.of(
                    new ItemCommand(this),
                    new ReloadCommand(this),
                    new SkullCommand(this)
            ).forEach(type -> root.then(type.literal()));

            event.registrar().register(root.build(), "The base command for Fusion!");
        });*/
    }

    public @NonNull final FusionPaper getFusion() {
        return this.fusion;
    }
}