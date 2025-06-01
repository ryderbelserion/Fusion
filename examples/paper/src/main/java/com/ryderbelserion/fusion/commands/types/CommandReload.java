package com.ryderbelserion.fusion.commands.types;

import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.fusion.config.Config;
import com.ryderbelserion.fusion.core.files.types.JaluCustomFile;
import com.ryderbelserion.fusion.core.files.types.JsonCustomFile;
import com.ryderbelserion.fusion.core.files.types.YamlCustomFile;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import dev.triumphteam.cmd.core.annotations.Syntax;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import java.nio.file.Path;

public class CommandReload extends BaseCommand {

    private final ComponentLogger logger = this.plugin.getComponentLogger();

    private final Path path = this.plugin.getDataPath();

    @Command("reload")
    @Permission(value = "fusion.reload", def = PermissionDefault.TRUE)
    @Syntax(value = "/fusion reload")
    public void reload(CommandSender sender) {
        this.fileManager.refresh(false);

        final JsonCustomFile json = this.fileManager.getJsonFile(this.path.resolve("actions.json"));

        if (json != null) {
            this.logger.warn("Json File: {}", json.getConfiguration().node("value").getBoolean(true));
        }

        final YamlCustomFile yaml = this.fileManager.getYamlFile(this.path.resolve("actions.yml"));

        if (yaml != null) {
            this.logger.warn("Yaml File: {}", yaml.getConfiguration().node("value").getBoolean(false));
        }

        final JaluCustomFile jalu = this.fileManager.getJaluFile(this.path.resolve("config.yml"));

        if (jalu != null) {
            final SettingsManager config = jalu.getConfiguration();

            this.logger.warn("Prefix: {}", config.getProperty(Config.prefix));
        }
    }
}