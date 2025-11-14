package com.ryderbelserion.fusion;

import com.ryderbelserion.fusion.core.utils.StringUtils;
import com.ryderbelserion.fusion.paper.FusionPaper;
import com.ryderbelserion.fusion.paper.files.PaperFileManager;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.HashMap;

public class Fusion extends JavaPlugin {

    private final FusionPaper fusion;

    public Fusion(@NotNull final FusionPaper fusion) {
        this.fusion = fusion;
    }

    @Override
    public void onEnable() {
        this.fusion.setPlugin(this).init();

        final PaperFileManager fileManager = this.fusion.getFileManager();

        final Path path = getDataPath();

        fileManager.extractFile("config.yml", path.resolve("guis").resolve("config.yml"));

        final ConsoleCommandSender sender = getServer().getConsoleSender();

        this.fusion.log("warn", "{} Chance: ", StringUtils.format(11.583011583011583));

        sender.sendMessage(this.fusion.parse(sender, "A test message with a placeholder: {test}", new HashMap<>() {{
            put("{test}", " <gradient:#e91e63:blue>CrazyCrates</gradient> | ");
        }}));
    }
}