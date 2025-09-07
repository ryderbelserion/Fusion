package com.ryderbelserion.fusion.paper;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.ryderbelserion.fusion.core.FusionConfig;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import com.ryderbelserion.fusion.core.api.support.ModSupport;
import com.ryderbelserion.fusion.core.FusionKey;
import com.ryderbelserion.fusion.core.api.support.objects.Mod;
import com.ryderbelserion.fusion.core.files.enums.FileAction;
import com.ryderbelserion.fusion.core.files.enums.FileType;
import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import com.ryderbelserion.fusion.paper.structure.StructureRegistry;
import com.ryderbelserion.fusion.paper.files.PaperFileManager;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public class FusionPaper extends FusionCore {

    private final PaperFileManager fileManager;
    private PluginManager pluginManager;
    private StructureRegistry registry;
    private HeadDatabaseAPI api;
    private JavaPlugin plugin;
    private Server server;

    public FusionPaper(@NotNull final JavaPlugin plugin) {
        super(consumer -> {
            consumer.setDataPath(plugin.getDataPath());

            consumer.setLogger(plugin.getComponentLogger());
        });

        this.fileManager = new PaperFileManager(this);

        this.plugin = plugin;

        this.server = this.plugin.getServer();

        this.pluginManager = this.server.getPluginManager();

        this.registry = new StructureRegistry(this.plugin, this.server.getStructureManager());
    }

    public FusionPaper(@NotNull final BootstrapContext context) {
        super(consumer -> {
            consumer.setDataPath(context.getDataDirectory());

            consumer.setLogger(context.getLogger());
        });

        this.fileManager = new PaperFileManager(this);
    }

    @Override
    public @NotNull final Component parse(@NotNull final Audience audience, @NotNull final String message, @NotNull final Map<String, String> placeholders, @NotNull final List<TagResolver> tags) {
        final List<TagResolver> resolvers = new ArrayList<>(tags);

        final com.ryderbelserion.fusion.core.utils.StringUtils utils = getStringUtils();

        placeholders.forEach((key, value) -> resolvers.add(Placeholder.parsed(utils.replaceAllBrackets(key).toLowerCase(), value)));

        return MiniMessage.miniMessage().deserialize(papi(audience, utils.replaceBrackets(message)), !resolvers.isEmpty() ? TagResolver.resolver(resolvers) : TagResolver.empty()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    @Override
    public @NotNull final String papi(@NotNull final Audience audience, @NotNull final String message) {
        return audience instanceof Player player && getModManager().getMod(ModSupport.placeholder_api).isEnabled() ? PlaceholderAPI.setPlaceholders(player, message) : message;
    }

    @Override
    public @NotNull final FusionPaper init(@NotNull final Consumer<FusionCore> fusion) {
        final Path dataPath = getDataPath();

        if (Files.notExists(dataPath)) {
            try {
                Files.createDirectory(dataPath);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        this.server = this.plugin.getServer();

        this.pluginManager = this.server.getPluginManager();

        this.registry = new StructureRegistry(this.plugin, this.server.getStructureManager());

        ModSupport.dependencies.forEach(dependency -> getModManager().addMod(dependency, new Mod()));

        this.fileManager.addFile(this.dataPath.resolve("fusion.yml"), FileType.FUSION_YAML, consumer -> {
            final YamlCustomFile customFile = (YamlCustomFile) consumer;

            customFile.setOptions(options -> options.shouldCopyDefaults(true));
            customFile.addAction(FileAction.EXTRACT_FILE);
        });

        this.config = new FusionConfig(this.fileManager.getYamlFile(this.dataPath.resolve("fusion.yml")));

        if (this.isModReady(ModSupport.head_database) && this.api == null) this.api = new HeadDatabaseAPI();

        fusion.accept(this);

        FusionProvider.register(this);

        return this;
    }

    @Override
    public @NotNull final FusionCore reload() {
        this.config.reload();

        return this;
    }

    @Override
    public @NotNull final String chomp(@NotNull final String value) {
        return StringUtils.chomp(value);
    }

    @Override
    public @NotNull final PlayerProfile createProfile(@NotNull final UUID uuid, @Nullable final String name) {
        return this.server.createProfile(uuid, name);
    }

    @Override
    public boolean isModReady(@NotNull final FusionKey key) {
        return this.pluginManager.isPluginEnabled(key.getValue());
    }

    @Override
    public PaperFileManager getFileManager() {
        return this.fileManager;
    }

    @Override
    public FusionConfig getConfig() {
        return this.config;
    }

    public @NotNull final StructureRegistry getRegistry() {
        if (this.registry == null) throw new FusionException("An error occurred while trying to get the structure registry instance.");

        return this.registry;
    }

    public @NotNull final Optional<HeadDatabaseAPI> getHeadDatabaseAPI() {
        return Optional.ofNullable(this.api);
    }

    public FusionPaper setJavaPlugin(@NotNull final JavaPlugin plugin) {
        this.plugin = plugin;

        return this;
    }
}