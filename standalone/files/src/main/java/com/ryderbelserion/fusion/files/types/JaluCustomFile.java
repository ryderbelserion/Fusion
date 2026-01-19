package com.ryderbelserion.fusion.files.types;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.interfaces.ICustomFile;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.function.Consumer;

public final class JaluCustomFile extends ICustomFile<JaluCustomFile, SettingsManager, SettingsManagerBuilder, YamlFileResourceOptions.Builder> {

    private final Consumer<SettingsManagerBuilder> builder;

    public JaluCustomFile(@NotNull final FileManager fileManager, @NotNull final Path path, @NotNull final Consumer<YamlFileResourceOptions.Builder> options, @NotNull final Consumer<SettingsManagerBuilder> builder) {
        super(fileManager, path, path);

        this.options = YamlFileResourceOptions.builder();

        options.accept(this.options);

        this.builder = builder;
    }

    @Override
    public @NotNull SettingsManager loadConfig() {
        if (this.configuration == null) {
            final SettingsManagerBuilder builder = SettingsManagerBuilder.withYamlFile(getPath(), this.options.build());

            builder.useDefaultMigrationService();

            this.builder.accept(builder); // overrides the default migration service if set in the consumer.

            return builder.create();
        }

        this.configuration.reload();

        return this.configuration;
    }

    @Override
    public void saveConfig() {
        this.configuration.save();
    }

    @Override
    public @NotNull FileType getFileType() {
        return FileType.JALU;
    }
}