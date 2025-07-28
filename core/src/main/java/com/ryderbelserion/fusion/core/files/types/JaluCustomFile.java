package com.ryderbelserion.fusion.core.files.types;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.core.files.FileManager;
import com.ryderbelserion.fusion.core.files.enums.FileType;
import com.ryderbelserion.fusion.core.files.interfaces.ICustomFile;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

public class JaluCustomFile extends ICustomFile<JaluCustomFile, SettingsManager, SettingsManagerBuilder, YamlFileResourceOptions> {

    public JaluCustomFile(@NotNull final FileManager fileManager, @NotNull final Consumer<JaluCustomFile> consumer) {
        super(fileManager);

        this.options = YamlFileResourceOptions.builder().build();

        consumer.accept(this);
    }

    @Override
    public @NotNull final SettingsManager loadConfig() {
        if (this.configuration == null) {
            return this.loader.create();
        }

        this.configuration.reload();

        return this.configuration;
    }

    @Override
    public final void saveConfig() {
        this.configuration.save();
    }

    @Override
    public FileType getFileType() {
        return FileType.JALU;
    }
}