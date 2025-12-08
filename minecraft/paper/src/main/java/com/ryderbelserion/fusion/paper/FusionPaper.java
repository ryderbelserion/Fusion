package com.ryderbelserion.fusion.paper;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.ryderbelserion.fusion.core.FusionCore;
import com.ryderbelserion.fusion.core.FusionKey;
import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.api.interfaces.permissions.enums.Mode;
import com.ryderbelserion.fusion.core.exceptions.FusionException;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.kyori.mods.ModSupport;
import com.ryderbelserion.fusion.kyori.mods.objects.Mod;
import com.ryderbelserion.fusion.paper.builders.gui.listeners.GuiListener;
import com.ryderbelserion.fusion.paper.files.PaperFileManager;
import com.ryderbelserion.fusion.paper.structure.StructureRegistry;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;

public class FusionPaper extends FusionKyori {

    private final PaperFileManager fileManager;
    private PluginManager pluginManager;
    private StructureRegistry registry;
    private HeadDatabaseAPI api;
    private JavaPlugin plugin;
    private Server server;

    private Path source;

    public FusionPaper(@NotNull final JavaPlugin plugin) {
        super(plugin.getDataPath(), plugin.getComponentLogger());
        
        try {
            final Method method = plugin.getClass().getDeclaredMethod("getFile");
            
            if (method.trySetAccessible()) {
                this.source = ((File) method.invoke(plugin)).toPath();
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        this.server = plugin.getServer();
        this.pluginManager = this.server.getPluginManager();
        this.fileManager = new PaperFileManager(this.source, getDataPath());

        this.plugin = plugin;

        this.registry = new StructureRegistry(this.plugin, this.server.getStructureManager());
    }

    public FusionPaper(@NotNull final BootstrapContext context) {
        super(context.getDataDirectory(), context.getLogger());

        this.fileManager = new PaperFileManager(this.source = context.getPluginSource(), getDataPath());

        FusionProvider.register(this);
    }

    @Override
    public @NotNull final FusionPaper init() {
        super.init();

        this.server = this.plugin.getServer();

        this.pluginManager = this.server.getPluginManager();

        this.registry = new StructureRegistry(this.plugin, this.server.getStructureManager());

        ModSupport.dependencies.forEach(dependency -> getModManager().addMod(dependency, new Mod(this)));

        if (this.isModReady(ModSupport.head_database) && this.api == null) this.api = new HeadDatabaseAPI();

        this.pluginManager.registerEvents(new GuiListener(), this.plugin);

        FusionProvider.register(this);

        return this;
    }

    @Override
    public @NotNull final Component parse(@Nullable final Audience audience, @NotNull final String message, @NotNull final Map<String, String> placeholders, @NotNull final List<TagResolver> tags) {
        return MiniMessage.miniMessage().deserialize(papi(audience, replacePlaceholder(message, placeholders))).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    @Override
    public @NotNull final PlayerProfile createProfile(@NotNull final UUID uuid, @Nullable final String name) {
        return this.server.createProfile(uuid, name);
    }

    @Override
    public @NotNull final String papi(@Nullable final Audience audience, @NotNull final String message) {
        return audience instanceof Player player && getModManager().getMod(ModSupport.placeholder_api).isEnabled() ? PlaceholderAPI.setPlaceholders(player, message) : message;
    }

    @Override
    public final boolean isModReady(@NotNull final FusionKey key) {
        return this.pluginManager.isPluginEnabled(key.getValue());
    }

    @Override
    public @NotNull final FusionCore reload() {
        this.config.reload();

        return this;
    }

    @Override
    public @NotNull final List<String> getFileNames(@NotNull final String folder, @NotNull final Path path, @NotNull final String extension, final int depth, final boolean removeExtension) {
        return this.fileManager.getFileNames(folder, path, extension, depth, removeExtension);
    }

    @Override
    public @NotNull final List<Path> getFiles(@NotNull final Path path, @NotNull final List<String> extensions) {
        return this.fileManager.getFiles(path, extensions, getDepth());
    }

    @Override
    public final boolean hasPermission(@NotNull final Audience audience, @NotNull final String permission) {
        final CommandSender sender = (CommandSender) audience;

        return sender.hasPermission(permission);
    }

    @Override
    public void registerPermission(@NotNull final Mode mode, @NotNull final String parent, @NotNull final String description, @NotNull final Map<String, Boolean> children) {
        PermissionDefault permissionDefault;

        switch (mode) {
            case NOT_OP -> permissionDefault = PermissionDefault.NOT_OP;
            case TRUE -> permissionDefault = PermissionDefault.TRUE;
            case FALSE -> permissionDefault = PermissionDefault.FALSE;
            default -> permissionDefault = PermissionDefault.OP;
        }

        if (isPermissionRegistered(parent)) return;

        final PluginManager pluginManager = this.server.getPluginManager();

        final Permission permission = new Permission(
                parent,
                description,
                permissionDefault,
                children
        );

        pluginManager.addPermission(permission);
    }

    @Override
    public void unregisterPermission(@NotNull final String parent) {
        if (!isPermissionRegistered(parent)) return;

        final PluginManager pluginManager = this.server.getPluginManager();

        pluginManager.removePermission(parent);
    }

    @Override
    public boolean isPermissionRegistered(@NotNull final String parent) {
        final PluginManager pluginManager = this.server.getPluginManager();

        return pluginManager.getPermission(parent) != null;
    }

    @Override
    public final @NotNull PaperFileManager getFileManager() {
        return this.fileManager;
    }

    public @NotNull final Optional<HeadDatabaseAPI> getHeadDatabaseAPI() {
        return Optional.ofNullable(this.api);
    }

    public @NotNull final StructureRegistry getRegistry() {
        if (this.registry == null) {
            throw new FusionException("An error occurred while trying to get the structure registry instance.");
        }

        return this.registry;
    }

    public @NotNull final FusionPaper setPlugin(@NotNull final JavaPlugin plugin) {
        this.plugin = plugin;

        return this;
    }

    public @NotNull final JavaPlugin getPlugin() {
        return this.plugin;
    }
}