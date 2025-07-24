package com.ryderbelserion.fusion.paper;

import com.ryderbelserion.fusion.core.FusionConfig;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.api.support.ModSupport;
import com.ryderbelserion.fusion.core.files.enums.FileAction;
import com.ryderbelserion.fusion.core.files.enums.FileType;
import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import com.ryderbelserion.fusion.paper.files.PaperFileManager;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FusionPaper extends FusionCore {

    private final PaperFileManager fileManager;
    private final PluginManager pluginManager;

    public FusionPaper(@NotNull final JavaPlugin plugin) {
        super(consumer -> {
            consumer.setDataPath(plugin.getDataPath());
        });

        this.fileManager = new PaperFileManager(this);

        init(consumer -> {
            this.config = new FusionConfig(this.fileManager.getYamlFile(Key.key("fusion")));
        });

        final Server server = plugin.getServer();

        this.pluginManager = server.getPluginManager();

        FusionProvider.register(this);
    }

    @Override
    public Component parse(@NotNull final Audience audience, @NotNull final String message, @NotNull final Map<String, String> placeholders, @NotNull final List<TagResolver> tags) {
        final List<TagResolver> resolvers = new ArrayList<>(tags);

        placeholders.forEach((key, value) -> resolvers.add(Placeholder.parsed(getStringUtils().replaceAllBrackets(key).toLowerCase(), value)));

        return MiniMessage.miniMessage().deserialize(papi(audience, message), !resolvers.isEmpty() ? TagResolver.resolver(resolvers) : TagResolver.empty()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    @Override
    public String papi(@NotNull final Audience audience, @NotNull final String message) {
        return audience instanceof Player player && getModManager().getMod(ModSupport.placeholder_api).isEnabled() ? PlaceholderAPI.setPlaceholders(player, message) : message;
    }

    @Override
    public FusionPaper init(@NotNull final Consumer<FusionCore> fusion) {
        final Path dataPath = getDataPath();

        if (Files.notExists(dataPath)) {
            try {
                Files.createDirectory(dataPath);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        this.fileManager.addFile(Key.key("fusion"), FileType.CONFIGURATE, consumer -> {
            final YamlCustomFile customFile = (YamlCustomFile) consumer;

            customFile.setOptions(options -> options.shouldCopyDefaults(true));
            customFile.setPath(dataPath.resolve("fusion.yml"));
            customFile.addAction(FileAction.EXTRACT_FILE);
        });

        fusion.accept(this);

        return this;
    }

    @Override
    public FusionCore reload() {
        this.config.reload();

        return this;
    }

    @Override
    public boolean isModReady(@NotNull final Key key) {
        return this.pluginManager.isPluginEnabled(key.value());
    }

    @Override
    public PaperFileManager getFileManager() {
        return this.fileManager;
    }

    @Override
    public FusionConfig getConfig() {
        return this.config;
    }
}