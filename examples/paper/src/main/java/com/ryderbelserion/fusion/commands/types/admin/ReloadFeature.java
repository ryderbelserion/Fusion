package com.ryderbelserion.fusion.commands.types.admin;

import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.fusion.commands.AnnotationFeature;
import com.ryderbelserion.fusion.config.Config;
import com.ryderbelserion.fusion.core.files.types.JaluCustomFile;
import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;
import org.jetbrains.annotations.NotNull;

public class ReloadFeature extends AnnotationFeature {

    @Override
    public void registerFeature(@NotNull final AnnotationParser<CommandSourceStack> parser) {
        parser.parse(this);
    }

    @Command("fusion reload")
    @CommandDescription("Reloads the plugin!")
    @Permission(value = "fusion.reload", mode = Permission.Mode.ANY_OF)
    public void tem(final CommandSender sender) {
        this.fileManager.refresh(false);

        final YamlCustomFile yaml = this.fileManager.getYamlFile(path.resolve("actions.yml"));

        if (yaml != null) {
            this.logger.warn("Yaml File: {}", yaml.getConfiguration().node("value").getBoolean(false));
        }

        final JaluCustomFile jalu = this.fileManager.getJaluFile(path.resolve("config.yml"));

        if (jalu != null) {
            final SettingsManager config = jalu.getConfiguration();

            this.logger.warn("Prefix: {}", config.getProperty(Config.prefix));
        }
    }
}