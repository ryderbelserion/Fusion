package com.ryderbelserion.fusion.commands;

import com.ryderbelserion.fusion.Fusion;
import com.ryderbelserion.fusion.commands.types.basic.HelpFeature;
import com.ryderbelserion.fusion.commands.types.basic.ItemFeature;
import com.ryderbelserion.fusion.commands.types.admin.ReloadFeature;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.injection.ParameterInjectorRegistry;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.List;

public class BaseCommand {

    private static final List<AnnotationFeature> features = Arrays.asList(
            new ReloadFeature(),
            new ItemFeature(),
            new HelpFeature()
    );

    private final Fusion plugin = JavaPlugin.getPlugin(Fusion.class);
    private final AnnotationParser<CommandSourceStack> parser;

    public BaseCommand(@NotNull final PaperCommandManager<CommandSourceStack> manager) {
        final ParameterInjectorRegistry<CommandSourceStack> injector = manager.parameterInjectorRegistry();

        injector.registerInjector(CommandSender.class, (context, accessor) -> context.sender().getSender());
        injector.registerInjector(Player.class, (context, accessor) -> {
            final CommandSender sender = context.sender().getSender();

            if (sender instanceof Player player) {
                return player;
            }

            sender.sendRichMessage("<red>You must be a player to run this command!</red>");

            return null;
        });

        this.parser = new AnnotationParser<>(manager, CommandSourceStack.class);

        register();
    }

    private void register() {
        features.forEach(feature -> feature.registerFeature(this.parser));
    }
}