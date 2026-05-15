package com.ryderbelserion.fusion.files.types;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.interfaces.ICustomFile;
import org.jspecify.annotations.NonNull;
import java.nio.file.Path;
import java.util.function.Consumer;

public final class JaluCustomFile extends ICustomFile<JaluCustomFile, SettingsManager, SettingsManagerBuilder, YamlFileResourceOptions.Builder> {

    private final Consumer<SettingsManagerBuilder> builder;

    public JaluCustomFile(@NonNull final FileManager fileManager, @NonNull final Path path, @NonNull final Consumer<YamlFileResourceOptions.Builder> options, @NonNull final Consumer<SettingsManagerBuilder> builder) {
        super(fileManager, path);

        this.options = YamlFileResourceOptions.builder();

        options.accept(this.options);

        this.builder = builder;
    }

    @Override
    public @NonNull SettingsManager loadConfig() {
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
    public @NonNull FileType getFileType() {
        return FileType.JALU;
    }
}