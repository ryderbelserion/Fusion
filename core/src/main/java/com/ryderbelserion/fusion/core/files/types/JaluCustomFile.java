package com.ryderbelserion.fusion.core.files.types;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.fusion.core.files.FileType;
import com.ryderbelserion.fusion.core.api.interfaces.IAbstractConfigFile;
import com.ryderbelserion.fusion.core.files.FileAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public class JaluCustomFile extends IAbstractConfigFile<JaluCustomFile, SettingsManager, YamlFileResourceOptions> {

    private final Consumer<SettingsManagerBuilder> builder;

    public JaluCustomFile(@NotNull final Path path, @NotNull Consumer<SettingsManagerBuilder> builder, @NotNull final List<FileAction> actions, @Nullable final YamlFileResourceOptions options) {
        super(path, actions, options == null ? YamlFileResourceOptions.builder().build() : options);

        this.builder = builder;
    }

    public JaluCustomFile(@NotNull final Path path, @NotNull final Consumer<SettingsManagerBuilder> builder, @NotNull final List<FileAction> actions) {
        this(path, builder, actions, null);
    }

    @Override
    public void loadConfig() {
        if (this.configuration == null) {
            final SettingsManagerBuilder builder = SettingsManagerBuilder.withYamlFile(getPath(), this.loader);

            builder.useDefaultMigrationService();

            this.builder.accept(builder); // overrides the default migration service if set in the consumer.

            this.configuration = builder.create();

            return;
        }

        this.configuration.reload();
    }

    @Override
    public void saveConfig() {
        this.configuration.save();
    }

    @Override
    public final boolean isLoaded() {
        return this.configuration != null;
    }

    @Override
    public final FileType getFileType() {
        return FileType.JALU;
    }
}