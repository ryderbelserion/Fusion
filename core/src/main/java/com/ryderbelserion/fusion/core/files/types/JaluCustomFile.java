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

/**
 * Loads, reloads, or saves existing files built using ConfigMe.
 */
public class JaluCustomFile extends IAbstractConfigFile<JaluCustomFile, SettingsManager, YamlFileResourceOptions> {

    private final Consumer<SettingsManagerBuilder> builder;

    /**
     * Constructs an {@code IAbstractConfigFile} with the specified file path, actions, and loader.
     *
     * @param path    the file path associated with the configuration file
     * @param builder the loader responsible for configuration management
     * @param actions the list of file actions applied to the configuration file
     * @param options the options responsible for configuring the formatting
     */
    public JaluCustomFile(@NotNull final Path path, @NotNull Consumer<SettingsManagerBuilder> builder, @NotNull final List<FileAction> actions, @Nullable final YamlFileResourceOptions options) {
        super(path, actions, options == null ? YamlFileResourceOptions.builder().build() : options);

        this.builder = builder;
    }

    /**
     * Constructs an {@code IAbstractConfigFile} with the specified file path, actions, and loader.
     *
     * @param path    the file path associated with the configuration file
     * @param builder the loader responsible for configuration management
     * @param actions the list of file actions applied to the configuration file
     */
    public JaluCustomFile(@NotNull final Path path, @NotNull final Consumer<SettingsManagerBuilder> builder, @NotNull final List<FileAction> actions) {
        this(path, builder, actions, null);
    }

    /**
     * Loads the configuration.
     *
     * <p>This method retrieves the configuration file and initializes its contents.</p>
     *
     * @return the loaded configuration instance
     */
    @Override
    public @NotNull SettingsManager loadConfig() {
        if (this.configuration == null) {
            final SettingsManagerBuilder builder = SettingsManagerBuilder.withYamlFile(getPath(), this.loader);

            builder.useDefaultMigrationService();

            this.builder.accept(builder); // overrides the default migration service if set in the consumer.

            return builder.create();
        }

        this.configuration.reload();

        return this.configuration;
    }

    /**
     * Saves the current configuration.
     *
     * <p>This method writes changes back to the configuration file.</p>
     *
     */
    @Override
    public void saveConfig() {
        this.configuration.save();
    }

    /**
     * Checks if the relative path exists.
     *
     * @return true or false
     */
    @Override
    public final boolean isLoaded() {
        return this.configuration != null;
    }

    /**
     * Retrieves the file type.
     *
     * @return the {@link FileType}
     */
    @Override
    public @NotNull final FileType getFileType() {
        return FileType.JALU;
    }
}