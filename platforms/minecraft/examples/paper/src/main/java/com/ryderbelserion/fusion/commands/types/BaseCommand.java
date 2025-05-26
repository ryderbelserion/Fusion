package com.ryderbelserion.fusion.commands.types;

import com.ryderbelserion.fusion.Fusion;
import com.ryderbelserion.fusion.core.files.FileManager;
import dev.triumphteam.cmd.core.annotations.Command;
import org.bukkit.plugin.java.JavaPlugin;

@Command("fusion")
public abstract class BaseCommand {

    protected final Fusion plugin = JavaPlugin.getPlugin(Fusion.class);

    protected final FileManager fileManager = this.plugin.getFileManager();

}