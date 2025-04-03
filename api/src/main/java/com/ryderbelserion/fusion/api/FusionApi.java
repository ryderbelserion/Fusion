package com.ryderbelserion.fusion.api;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.fusion.api.configs.ConfigManager;
import com.ryderbelserion.fusion.api.configs.keys.ConfigKeys;
import com.ryderbelserion.fusion.api.files.FileManager;
import com.ryderbelserion.fusion.api.interfaces.ILogger;
import java.nio.file.Path;

public abstract class FusionApi {

    private ConfigManager configManager;
    private FileManager fileManager;
    protected SettingsManager config;

    public abstract Path getDataFolder();

    public abstract ILogger getLogger();

    public abstract String chomp(final String message);

    public String getNumberFormat() {
        return this.config.getProperty(ConfigKeys.number_format);
    }

    public String getRounding() {
        return this.config.getProperty(ConfigKeys.rounding);
    }

    public boolean isVerbose() {
        return this.config.getProperty(ConfigKeys.is_verbose);
    }

    public int getDepth() {
        return this.config.getProperty(ConfigKeys.recursion_depth);
    }

    @SuppressWarnings("unchecked")
    public void init(final Class<? extends SettingsHolder>... classes) {
        this.configManager = new ConfigManager(getDataFolder()); // create config manager
        this.configManager.init(classes); // load configs

        this.config = this.configManager.getConfig(); // assign it

        this.fileManager = new FileManager();
    }

    public void save() {

    }

    public void reload() {
        this.configManager.reload(); // reload the configs
    }

    public FileManager getFileManager() {
        return this.fileManager;
    }

    public static class Provider {
        private static FusionApi core = null;

        public static void register(final FusionApi core) {
            Provider.core = core;
        }

        public static FusionApi get() {
            return core;
        }
    }
}