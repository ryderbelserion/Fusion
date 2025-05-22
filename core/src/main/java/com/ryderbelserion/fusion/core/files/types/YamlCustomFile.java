package com.ryderbelserion.fusion.core.files.types;

import com.ryderbelserion.fusion.core.files.FileType;
import com.ryderbelserion.fusion.core.api.interfaces.IAbstractConfigFile;
import com.ryderbelserion.fusion.core.files.FileAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import java.nio.file.Path;
import java.util.List;
import java.util.function.UnaryOperator;

public class YamlCustomFile extends IAbstractConfigFile<YamlCustomFile, CommentedConfigurationNode, YamlConfigurationLoader> {

    public YamlCustomFile(@NotNull final Path path, @NotNull final List<FileAction> actions, @Nullable final UnaryOperator<ConfigurationOptions> options) {
        super(path, actions, YamlConfigurationLoader.builder().defaultOptions(options != null ? options : configurationOptions -> configurationOptions).indent(2).path(path).build());
    }

    @Override
    public CommentedConfigurationNode loadConfig() throws ConfigurateException {
        return this.loader.load();
    }

    @Override
    public void saveConfig() throws ConfigurateException {
        this.loader.save(this.configuration);
    }

    @Override
    public FileType getFileType() {
        return FileType.YAML;
    }
}