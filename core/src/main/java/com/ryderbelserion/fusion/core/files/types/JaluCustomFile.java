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

    private final Consumer<SettingsManagerBuilder> builder;

    public JaluCustomFile(@NotNull final FileManager fileManager, @NotNull final Consumer<SettingsManagerBuilder> builder) {
        super(fileManager);

        this.builder = builder;
    }

    @Override
    public @NotNull final SettingsManager loadConfig() {
        if (this.configuration == null) {
            final SettingsManagerBuilder builder = SettingsManagerBuilder.withYamlFile(getPath(), this.options);

            builder.useDefaultMigrationService();

            this.builder.accept(builder); // overrides the default migration service if set in the consumer.

            return builder.create();
        }

        this.configuration.reload();

        return this.configuration;
    }

    @Override
    public final void saveConfig() {
        this.configuration.save();
    }

    @Override
    public @NotNull final FileType getFileType() {
        return FileType.JALU;
    }
}