package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.commands.BaseCommand;
import com.ryderbelserion.fusion.core.api.FusionKey;
import com.ryderbelserion.fusion.core.api.registry.message.MessageRegistry;
import com.ryderbelserion.fusion.core.api.registry.message.adapter.YamlMessageAdapter;
import com.ryderbelserion.fusion.enums.FileKeys;
import com.ryderbelserion.fusion.kyori.commands.api.senders.MetaKeys;
import com.ryderbelserion.fusion.paper.commands.PaperCommandManager;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.List;
import java.util.Map;

public class Fusion extends JavaPlugin implements Listener {

    public static @NotNull Fusion getFusion() {
        return JavaPlugin.getPlugin(Fusion.class);
    }

    private FusionPaper fusion;

    @Override
    public void onEnable() {
        this.fusion = new FusionPaper(this);
        this.fusion.init();

        FileKeys.messages.init();

        final MessageRegistry registry = this.fusion.getMessageRegistry();

        registry.init();

        final CommentedConfigurationNode configuration = FileKeys.messages.getYamlConfig();

        final FusionKey defaultKey = registry.getDefaultKey();

        registry.addKey(defaultKey, MetaKeys.must_be_player, new YamlMessageAdapter(configuration,
                "{prefix}<red>You must be a player to use this command.",
                Map.of("{prefix}", "<yellow>Fusion "),
                "messages", "player", "requirements", "must-be-player"));
        registry.addKey(defaultKey, MetaKeys.must_be_console_sender, new YamlMessageAdapter(configuration,
                "{prefix}<red>You must be using console to use this command.",
                Map.of("{prefix}", "<yellow>Fusion "),
                "messages", "player", "requirements", "must-be-console-sender"));

        final PaperCommandManager manager = this.fusion.getCommandManager();

        List.of(
                new BaseCommand()
        ).forEach(manager::parse);
    }
}