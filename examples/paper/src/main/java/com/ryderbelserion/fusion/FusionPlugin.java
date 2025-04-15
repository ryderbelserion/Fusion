package com.ryderbelserion.fusion;

import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.core.api.ConfigKeys;
import com.ryderbelserion.fusion.core.managers.files.FileManager;
import com.ryderbelserion.fusion.paper.FusionPaper;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class FusionPlugin extends JavaPlugin {

    public static FusionPlugin getPlugin() {
        return JavaPlugin.getPlugin(FusionPlugin.class);
    }

    private FusionPaper api = null;

    @Override
    public void onEnable() {
        build(consumer -> consumer.configurationData(ConfigKeys.class), YamlFileResourceOptions.builder().indentationSize(2).build());

        /*this.api = new FusionPaper(getComponentLogger(), getDataPath());
        this.api.enable(this);*/
    }

    public void build(@NotNull final Consumer<SettingsManagerBuilder> consumer, @NotNull final YamlFileResourceOptions options) {
        final SettingsManagerBuilder builder = SettingsManagerBuilder.withYamlFile(getDataPath().resolve("fusion.yml"), options);

        consumer.accept(builder);

        builder.create();
    }

    public FusionPaper getApi() {
        return this.api;
    }
}